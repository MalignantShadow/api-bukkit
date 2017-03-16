package info.malignantshadow.api.bukkit.toys.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;

public class VirtualInventoryListener implements Listener {
	
	private VirtualInventory getInventory(InventoryEvent event) {
		if (!(event.getInventory().getHolder() instanceof VirtualInventory))
			return null;
		
		return (VirtualInventory) event.getInventory().getHolder();
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		VirtualInventory inv = getInventory(event);
		if (inv == null)
			return;
		
		inv.onClick(event);
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		VirtualInventory inv = getInventory(event);
		if (inv == null)
			return;
		
		inv.onDrag(event);
	}
	
}
