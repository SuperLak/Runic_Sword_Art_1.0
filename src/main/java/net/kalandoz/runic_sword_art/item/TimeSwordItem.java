package net.kalandoz.runic_sword_art.item;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TimeSwordItem extends SwordItem {
    public TimeSwordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof PlayerEntity) {
            if(!worldIn.isRemote) {
                ServerPlayerEntity player = ((ServerPlayerEntity) entityIn);
                if(!stack.hasTag()) {
                    if(player.getAdvancements().getProgress(Advancement.Builder.builder().
                            build(new ResourceLocation("story/lava_bucket"))).isDone()) {
                        CompoundNBT tag = new CompoundNBT();
                        tag.putBoolean(RunicSwordArt.MOD_ID + ".hadAdvancement", true);
                        stack.setTag(tag);
                    }
                }
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn,
                               List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        if(stack.hasTag()) {
            if(stack.hasTag() && stack.getTag().getBoolean(RunicSwordArt.MOD_ID + ".hadAdvancement")) {
                tooltip.add(new StringTextComponent("You are a poopyhead!"));
            }
        } else {
            tooltip.add(new StringTextComponent("The secrets of the Staff have not been revealed!"));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // applies speed effect to attacker for 20 seconds
        attacker.addPotionEffect(new EffectInstance(Effects.SPEED, 200,2));
        // applies slowness effect to target for 20 seconds
        target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200, 2));
        // consumes 20 durability from the weapon
        stack.damageItem(20, attacker, player -> player.sendBreakAnimation(attacker.getActiveHand()));
        return true;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public static float getArrowVelocity(int charge) {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
}