package net.kalandoz.runic_sword_art.entity;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.entity.projectile.IceSpellEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.ENTITIES, RunicSwordArt.MOD_ID);

    public static final RegistryObject<EntityType<IceSpellEntity>> ICE_SPELL =
            ENTITY_TYPES.register("ice_spell",
                    () -> EntityType.Builder.<IceSpellEntity>create(IceSpellEntity::new,
                                    EntityClassification.MISC).size(0.3f, 0.3f).trackingRange(3).updateInterval(20)
                            .build(new ResourceLocation(RunicSwordArt.MOD_ID, "ice_spell").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}