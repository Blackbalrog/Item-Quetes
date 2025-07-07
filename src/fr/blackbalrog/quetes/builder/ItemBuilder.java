package fr.blackbalrog.quetes.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import fr.blackbalrog.quetes.Quetes;

public class ItemBuilder
{
	
	private static ItemBuilder itemBuilder;
	
	private ItemStack item;
	private ItemMeta meta;
	
	public ItemBuilder(Material material)
	{
		itemBuilder = this;
		
		this.item = new ItemStack(material);
		this.meta = this.item.getItemMeta();
	}
	
	public ItemBuilder(ItemStack item)
	{
		itemBuilder = this;
		this.item = item;
		this.meta = item.getItemMeta();
	}
	
	public ItemBuilder setName(String name)
	{
		this.meta.setDisplayName(name);
		return this;
	}
	
	public String getName()
	{
		return this.meta.getDisplayName();
	}
	
	public ItemBuilder setLores(List<String> lores)
	{
		this.meta.setLore(lores.stream()
				.map(lore -> lore.replaceAll("&", "§"))
				.collect(Collectors.toList()));
		return this;
	}
	
	public List<String> getLores()
	{
		return this.meta.getLore();
	}
	
	public ItemBuilder setAmount(int count)
	{
		this.item.setAmount(count);
		return this;
	}
	
	public ItemBuilder setByteTag(String tag, byte value)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		this.meta.getPersistentDataContainer().set(keyTag, PersistentDataType.BYTE, value);
		return this;	
	}
	
	public byte getByteTag(String tag)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		return this.meta.getPersistentDataContainer().get(keyTag, PersistentDataType.BYTE);
	}
	
	public ItemBuilder setIntTag(String tag, int value)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		this.meta.getPersistentDataContainer().set(keyTag, PersistentDataType.INTEGER, value);
		return this;
	}
	
	public int getIntTag(String tag)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		return this.meta.getPersistentDataContainer().get(keyTag, PersistentDataType.INTEGER);
	}
	
	public ItemBuilder setDoubleTag(String tag, double value)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		this.meta.getPersistentDataContainer().set(keyTag, PersistentDataType.DOUBLE, value);
		return this;
	}
	
	public double getDoubleTag(String tag)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		return this.meta.getPersistentDataContainer().get(keyTag, PersistentDataType.DOUBLE);
	}
	
	public ItemBuilder setFloatTag(String tag, float value)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		this.meta.getPersistentDataContainer().set(keyTag, PersistentDataType.FLOAT, value);
		return this;
	}
	
	public float getFloatTag(String tag)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		return this.meta.getPersistentDataContainer().get(keyTag, PersistentDataType.FLOAT);
	}
	
	public ItemBuilder setLongTag(String tag, long value)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		this.meta.getPersistentDataContainer().set(keyTag, PersistentDataType.LONG, value);
		return this;
	}
	
	public long getLongTag(String tag)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		return this.meta.getPersistentDataContainer().get(keyTag, PersistentDataType.LONG);
	}
	
	public ItemBuilder setShortTag(String tag, short value)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		this.meta.getPersistentDataContainer().set(keyTag, PersistentDataType.SHORT, value);
		return this;
	}
	
	public long getShortTag(String tag)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		return this.meta.getPersistentDataContainer().get(keyTag, PersistentDataType.SHORT);
	}
	
	public ItemBuilder setBooleanTag(String tag, boolean value)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		this.meta.getPersistentDataContainer().set(keyTag, PersistentDataType.BOOLEAN, value);
		return this;
	}
	
	public boolean getBooleanTag(String tag)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		Boolean value = this.meta.getPersistentDataContainer().get(keyTag, PersistentDataType.BOOLEAN);
		return value != null ? value : false;
	}
	
	public ItemBuilder setStringTag(String tag, String value)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		this.meta.getPersistentDataContainer().set(keyTag, PersistentDataType.STRING, value);
		return this;
	}
	
	public String getStringTag(String tag)
	{
		NamespacedKey keyTag = new NamespacedKey(Quetes.getInstance(), tag);
		return this.meta.getPersistentDataContainer().get(keyTag, PersistentDataType.STRING);
	}

	public ItemBuilder removeTag(String key)
	{
		if (this.meta instanceof PersistentDataHolder)
		{
			PersistentDataContainer container = ((PersistentDataHolder) this.meta).getPersistentDataContainer();
			NamespacedKey namespacedKey = new NamespacedKey(Quetes.getInstance(), key);
			container.remove(namespacedKey);
		}
		return this;
	}

	
	public Map<String, Object> getTags()
	{
		Map<String, Object> tags = new HashMap<>();
		PersistentDataContainer container = this.meta.getPersistentDataContainer();

		for (NamespacedKey key : container.getKeys())
		{
			if (container.has(key, PersistentDataType.INTEGER)) tags.put(key.getKey(), container.get(key, PersistentDataType.INTEGER));
			else if (container.has(key, PersistentDataType.BYTE)) tags.put(key.getKey(), container.get(key, PersistentDataType.BYTE));
			else if (container.has(key, PersistentDataType.LONG)) tags.put(key.getKey(), container.get(key, PersistentDataType.LONG));
			else if (container.has(key, PersistentDataType.SHORT)) tags.put(key.getKey(), container.get(key, PersistentDataType.SHORT));
			else if (container.has(key, PersistentDataType.FLOAT)) tags.put(key.getKey(), container.get(key, PersistentDataType.FLOAT));
			else if (container.has(key, PersistentDataType.DOUBLE)) tags.put(key.getKey(), container.get(key, PersistentDataType.DOUBLE));
			else if (container.has(key, PersistentDataType.BOOLEAN)) tags.put(key.getKey(), container.get(key, PersistentDataType.BOOLEAN));
			else if (container.has(key, PersistentDataType.STRING)) tags.put(key.getKey(), container.get(key, PersistentDataType.STRING));
		}
		return tags;
	}
	
	public void build()
	{
		this.item.setItemMeta(this.meta);
	}
	
	public ItemStack getItemStack()
	{
		return this.item;
	}
	
	public ItemMeta getItemMeta()
	{
		return this.meta;
	}
	
	public static ItemBuilder getItemBuilder()
	{
		return itemBuilder;
	}
}
