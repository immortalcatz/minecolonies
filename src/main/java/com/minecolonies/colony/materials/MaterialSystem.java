package com.minecolonies.colony.materials;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.*;

/**
 * Controller class for the whole material system.
 * Created: December 14, 2015
 *
 * @author Colton
 */
public class MaterialSystem
{
    /**
     * Temporary variable to disabled MaterialHandling until I have time to complete it - Colton
     */
    public static final boolean isEnabled = false;

    /**
     * This Map contains keeps track of how many extra materials we have in the colony. (Materials that aren't needed)
     */
    private Map<Material, Integer> materials = new HashMap<>();

    /**
     * Set of MaterialStores inside this MaterialSystem(Colony)
     */
    private Set<MaterialStore> stores = new HashSet<>();

    /**
     * So that we only have one Material reference per material inside of the system.
     */
    private Map<Integer, Material> materialCache = new HashMap<>();

    /**
     * @return An unmodifiable version of the materials map
     */
    public Map<Material, Integer> getMaterials()
    {
        return Collections.unmodifiableMap(materials);
    }

    /**
     * @return An unmodifiable version of the stores set
     */
    public Set<MaterialStore> getStores()
    {
        return Collections.unmodifiableSet(stores);
    }

    /**
     * Finds how much extra(unneeded) items we have in the system(colony).
     *
     * @param item Item you want to know how much of you have
     * @return The number of unneeded item that is in the colony
     */
    public int getMaterialCount(Item item)
    {
        return getMaterialCount(getMaterial(item));
    }

    /**
     * Finds how much extra(unneeded) blocks we have in the system(colony).
     *
     * @param block Block you want to know how much of you have
     * @return The number of unneeded block that is in the colony
     */
    public int getMaterialCount(Block block)
    {
        return getMaterialCount(getMaterial(block));
    }

    private int getMaterialCount(Material material)
    {
        Integer count = materials.get(material);

        if(count == null)
        {
            removeItemFromCache(material);
            return 0;
        }

        return count;
    }

    /**
     * Gets a material from the cache, or create it if it doesn't exist.
     *
     * @return Material from cache
     */
    private Material getMaterial(Integer id)
    {
        Material material = materialCache.get(id);
        if(material == null)
        {
            material = new Material(id);
            materialCache.put(id, material);
        }

        return material;
    }

    Material getMaterial(Item item)
    {
        if(item == null) return null;

        return getMaterial(Item.getIdFromItem(item));
    }

    Material getMaterial(Block block)
    {
        if(block == null) return null;

        return getMaterial(Block.getIdFromBlock(block));
    }

    private void removeItemFromCache(Material material)
    {
        materialCache.remove(material.getID());
    }

    /**
     * Adds a material to the system.
     *
     * @param material What material you're adding
     * @param quantity How much you're adding
     */
    void addMaterial(Material material, int quantity)
    {
        Integer count = materials.get(material);
        if(count == null)
        {
            materials.put(material, quantity);
        }
        else
        {
            materials.put(material, quantity + count);
        }
    }

    /**
     * Removes material from the system.
     *
     * @param material What material you're removing
     * @param quantity How much you're removing
     */
    void removeMaterial(Material material, int quantity)
    {
        Integer count = materials.get(material);
        if(count == null || count < quantity)
        {
            throw new QuantityNotFound("MaterialSystem", material.getID(), count == null ? 0 : count, quantity);
        }
        else if(count == quantity)
        {
            materials.remove(material);
            removeItemFromCache(material);
        }
        else
        {
            materials.put(material, count - quantity);
        }
    }

    /**
     * Adds a MaterialStore to the stores set. Called inside of the MaterialStore constructor.
     *
     * @param store
     */
    void addStore(MaterialStore store)
    {
        stores.add(store);
    }

    /**
     * Removes a MaterialStore from the store set. This should be called when a building is destroyed(removed from colony).
     *
     * @param store
     */
    void removeStore(MaterialStore store)
    {
        stores.remove(store);
    }
}
