package info.malignantshadow.api.bukkit.toys.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import info.malignantshadow.api.bukkit.helpers.BukkitMessages;

public class InventoryMenu extends VirtualInventory {
	
	private Runnable[] _actions;
	
	public InventoryMenu(String title, int rows) {
		super(title, rows * 9);
		_actions = new Runnable[getInventory().getSize()];
	}
	
	@Override
	protected void onClick(InventoryClickEvent event) {
		if (rawSlotIsBottom(event.getRawSlot(), event.getView()) || event.getClick().isKeyboardClick())
			return;
		
		Runnable r = _actions[event.getRawSlot()];
		if (r != null)
			r.run();
	}
	
	public void setItem(int slot, Material m) {
		setItem(slot, m, 1);
	}
	
	public void setItem(int slot, Material m, int amount) {
		setItem(slot, m, 0, amount);
	}
	
	public void setItem(int slot, Material m, int damage, int amount) {
		getInventory().setItem(slot, new ItemStack(m, amount, (short) damage));
	}
	
	public void removeItem(int slot) {
		getInventory().setItem(slot, null);
	}
	
	public void remove(int slot) {
		removeItem(slot);
		removeAction(slot);
	}
	
	public void setItemTitle(int slot, String title) {
		ItemStack item = getInventory().getItem(slot);
		if (item == null)
			return;
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(BukkitMessages.format(title));
		item.setItemMeta(meta);
	}
	
	public void setItemDescription(int slot, String... desc) {
		ItemStack item = getInventory().getItem(slot);
		if (item == null)
			return;
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		for (String s : desc)
			if (s != null)
				lore.add(BukkitMessages.format(s));
			
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public void setAction(int slot, Runnable action) {
		_actions[slot] = action;
	}
	
	public void removeAction(int slot) {
		_actions[slot] = null;
	}
	
	@Override
	protected void onDrag(InventoryDragEvent event) {
		if (dragIsTop(event) || dragIsBoth(event)) {
			event.setCancelled(false);
			return;
		}
		
	}
	
}
