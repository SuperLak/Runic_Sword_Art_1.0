package net.kalandoz.runic_sword_art.item;

import com.google.common.collect.Multimap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class FlameSwordItem extends SwordItem {
    public FlameSwordItem(IItemTier tier, int p_i48460_2_, float p_i48460_3_, Properties p_i48460_4_) {
        super(tier, p_i48460_2_, p_i48460_3_, p_i48460_4_);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        /*
        displays information about the item
         */
        if (Screen.hasShiftDown()) {
            // prompts user to hold shift
            tooltip.add(new TranslationTextComponent("tooltip.runic_sword_art.flame_sword_shift"));
        } else {
            // displays short summary of item's abilities
            tooltip.add(new TranslationTextComponent("tooltip.runic_sword_art.flame_sword"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // sets target on fire for a number of seconds
        target.setFire(5);
        stack.damageItem(1, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    private void lightGroundOnFire(ItemUseContext context) {
        PlayerEntity playerentity = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockpos = context.getPos().offset(context.getFace());
        // checking to see if the target can be lit on fire
        if (AbstractFireBlock.canLightBlock(world, blockpos, context.getPlacementHorizontalFacing())) {
            // playing the sound of flint and steel being used
            world.playSound(playerentity, blockpos, SoundEvents.ITEM_FLINTANDSTEEL_USE,
                    SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
            BlockState blockstate = AbstractFireBlock.getFireForPlacement(world, blockpos);
            // setting block on fire
            world.setBlockState(blockpos, blockstate, 11);
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        /*
        lights ground on fire like flint and steel
         */
        // retrieves an instance of world from context to later check if world is remote
        // (to prevent code triggering twice)
        World world = context.getWorld();
        // retrieves an instance of playerEntity from context to later apply fire resistance
        PlayerEntity playerEntity = Objects.requireNonNull(context.getPlayer());
        // retrieves an instance of itemStack from context to later consume durability
        ItemStack stack = context.getItem();
        if(!world.isRemote) {
            // activates if the player isn't crouching
            if(!playerEntity.isCrouching()) {
                // light ground on fire
                lightGroundOnFire(context);
                // damages item (and breaks it if it would break)
                stack.damageItem(1, playerEntity, player -> player.sendBreakAnimation(context.getHand()));
            }
            // activates if the player is crouching
            else {
                // activates if the player has fire resistance
                if(playerEntity.isPotionActive(Effects.FIRE_RESISTANCE)) {
                    // removes any previous fire resistance effects (so that the following effect can be applied)
                    playerEntity.removePotionEffect(Effects.FIRE_RESISTANCE);
                    // give the player fire resistance for 2 minutes
                    playerEntity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 2400));
                    // consumes 150 durability from the flame sword
                    stack.damageItem(150, playerEntity, player -> player.sendBreakAnimation(context.getHand()));
                }
                // activates if the player doesn't have fire resistance
                else {
                    // gives the player fire resistance for 30 seconds
                    playerEntity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 600));
                    // consumes 50 durability from the flame sword
                    stack.damageItem(50, playerEntity, player -> player.sendBreakAnimation(context.getHand()));
                }
            }
        }
        return super.onItemUse(context);
    }
}
