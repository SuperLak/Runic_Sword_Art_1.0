package net.kalandoz.runic_sword_art.item;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.entity.projectile.IceSpellEntity;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.item.BowItem.getArrowVelocity;

public class IceSwordItem extends ShootableItem {
    public IceSwordItem(Properties properties) {
        super(properties);
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)entityLiving;

            if (!worldIn.isRemote) {
                int i = this.getUseDuration(stack) - timeLeft;
                float f = getArrowVelocity(i);

                IceSpellEntity iceSpell = new IceSpellEntity(worldIn, playerentity);
                iceSpell.setDirectionAndMovement(playerentity, playerentity.rotationPitch, playerentity.rotationYaw,
                        0.0F, f * 3.0F, 1.0F);

                stack.damageItem(10, playerentity, (player) -> {
                    player.sendBreakAnimation(playerentity.getActiveHand());
                });
            }
        }
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
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Here we can add effects on hit with this weapon to the target
        target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 600, 3));
        return super.hitEntity(stack, target, attacker);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(itemstack);
    }

    public int getUseDuration(ItemStack stack) {
        return 7200;
    }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return (itemStack) -> true;
    }

    @Override
    public int func_230305_d_() {
        return 15;
    }




}