package info.malignantshadow.api.bukkit.players;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class BukkitWorld {
	
	public static final float DEF_EXPLOSION_POWER = 4;
	
	private static void checkLocation(Location loc) {
		if (loc == null || loc.getWorld() == null)
			throw new IllegalArgumentException("Location must be non-null and have a non-null world");
	}
	
	public static Location getHighestNonAirLocation(Location location) {
		checkLocation(location);
		Location l = location.clone();
		l.setY(location.getWorld().getHighestBlockYAt(location));
		return l;
	}
	
	public static Location getSafePosition(Location location) {
		return getSafePosition(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	/**
	 * Get a safe location for a player (two non-solid blocks on top of each other), looking in an upward direction from the given location.
	 * 
	 * @param world
	 *            The world
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @param z
	 *            The z coordinate
	 * @return A safe location (which may be equal to the given location) or {@code null} if there wasn't any.
	 */
	public static Location getSafePosition(World world, int x, int y, int z) {
		if (world == null)
			return null;
		
		y = Math.max(0, y);
		byte free = 0;
		
		while (y <= world.getMaxHeight() + 2) {
			Block b = world.getBlockAt(x, y, z);
			if (b == null || !b.getType().isSolid())
				free++;
			else
				free = 0;
			
			if (free == 2)
				return new Location(world, x + 0.5, y - 1, z + 0.5);
		}
		
		return null;
	}
	
	public static void lightning(Location location) {
		lightning(location, false);
	}
	
	public static void lightning(Location location, boolean effect) {
		lightning(location, effect, true);
	}
	
	public static void lightning(Location location, boolean effect, boolean realistic) {
		checkLocation(location);
		if (realistic)
			location = getHighestNonAirLocation(location);
		
		if (effect)
			location.getWorld().strikeLightning(location);
		else
			location.getWorld().strikeLightningEffect(location);
	}
	
	public static void explosion(Location location) {
		explosion(location, DEF_EXPLOSION_POWER); // 4 = TNT
	}
	
	public static void explosion(Location location, float power) {
		explosion(location, power, false, true);
	}
	
	public static void explosionFire(Location location) {
		explosionFire(location, DEF_EXPLOSION_POWER);
	}
	
	public static void explosionFire(Location location, float power) {
		explosionFire(location, power, true);
	}
	
	public static void explosionFire(Location location, float power, boolean breakBlocks) {
		explosion(location, power, true, breakBlocks);
	}
	
	public static void explosionNerfed(Location location) {
		explosionNerfed(location, DEF_EXPLOSION_POWER);
	}
	
	public static void explosionNerfed(Location location, float power) {
		explosionNerfed(location, power, false);
	}
	
	public static void explosionNerfed(Location location, float power, boolean fire) {
		explosion(location, power, fire, false);
	}
	
	public static void explosion(Location location, float power, boolean fire, boolean breakBlocks) {
		checkLocation(location);
		location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), power, fire, breakBlocks);
	}
	
	public static List<Entity> spawn(EntityType type, Location location) {
		return spawn(type, location, 1);
	}
	
	public static List<Entity> spawn(EntityType type, Location location, int amount) {
		checkLocation(location);
		if (type == null || !type.isSpawnable())
			throw new IllegalArgumentException("type cannot be null and must be spawnable (EntityType.isSpawnable())");
		if (amount <= 0)
			throw new IllegalArgumentException("amount must be non-zero and positive");
		
		List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < amount; i++)
			entities.add(location.getWorld().spawn(location, type.getEntityClass()));
		return entities;
	}
	
	public static boolean differentBlocks(Location from, Location to) {
		if (from == null && to == null) //both are null
			return false;
		
		if ((from == null) != (to == null)) //one is null
			return true;
		
		return from.getWorld() != to.getWorld() ||
			from.getBlockX() != to.getBlockX() ||
			from.getBlockY() != to.getBlockY() ||
			from.getBlockZ() != to.getBlockZ();
	}
	
}
