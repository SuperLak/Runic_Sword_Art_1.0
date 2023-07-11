package net.kalandoz.runic_sword_art.entity.projectile;

import net.kalandoz.runic_sword_art.entity.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;

public class IceSpellEntity extends AbstractArrowEntity {

    public IceSpellEntity(EntityType<? extends IceSpellEntity> entityEntityType, World world) {
        super(ModEntityTypes.ICE_SPELL.get(), world);
    }

    public IceSpellEntity(World world, LivingEntity shooter) {
        super(ModEntityTypes.ICE_SPELL.get(), shooter, world);
    }

    public IPacket<?> createSpawnPacket() {
        Entity entity = this.getShooter();
        return new SSpawnObjectPacket(this, entity == null ? 0 : entity.getEntityId());
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.AIR);
    }
    }