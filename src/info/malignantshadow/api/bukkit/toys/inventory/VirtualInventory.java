package info.malignantshadow.api.bukkit.toys.inventory;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

import info.malignantshadow.api.bukkit.helpers.BukkitMessages;

public class VirtualInventory implements InventoryHolder {
	
	private Inventory _inv;
	
	public VirtualInventory(String title, int rows) {
		_inv = Bukkit.createInventory(this, rows * 9, BukkitMessages.format(title));
	}
	
	protected void onClick(InventoryClickEvent event) {
		
	}
	
	protected void onDrag(InventoryDragEvent event) {
		
	}
	
	protected boolean dragIsTop(InventoryDragEvent event) {
		for (int i : event.getRawSlots())
			if (rawSlotIsBottom(i, event.getView()))
				return false;
			
		return true;
	}
	
	protected boolean dragIsBottom(InventoryDragEvent event) {
		for (int i : event.getRawSlots())
			if (rawSlotIsTop(i, event.getView()))
				return false;
			
		return true;
	}
	
	protected boolean rawSlotIsTop(int slot, InventoryView view) {
		return !rawSlotIsBottom(slot, view);
	}
	
	protected boolean rawSlotIsBottom(int slot, InventoryView view) {
		return slot < view.getBottomInventory().getSize();
	}
	
	protected boolean dragIsBoth(InventoryDragEvent event) {
		boolean top = false;
		boolean bottom = false;
		for (int i : event.getRawSlots()) {
			if (rawSlotIsBottom(i, event.getView()))
				bottom = true;
			else
				top = true;
			
			if (top && bottom)
				return true;
		}
		return false;
	}
	
	@Override
	public Inventory getInventory() {
		return _inv;
	}
	
}
