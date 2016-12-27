package info.malignantshadow.api.bukkit.helpers;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

/**
 * Represents a player on a Bukkit server. This class holds now reference no any {@link Player} object, so instances
 * of this class can be kept in any sort of collection.
 * 
 * <p>
 * Instead, a reference to the player's name and UUID are stored. Whenever the actual handle is needed, {@link #getHandle()} will be called, which
 * will retrieve the {@link Player} object (if any) using the stored UUID.
 * </p>
 * 
 * @author MalignantShadow (Caleb Downs)
 *
 */
public class BukkitPlayer {
	
	public static final int DEF_RANGE = 500;
	
	private String _name;
	private UUID _id;
	
	/**
	 * Construct a new instance using a UUID. An {@link OfflinePlayer} object will be retrieved from Bukkit.
	 * 
	 * @param uuid
	 *            The UUID
	 * @see Bukkit#getOfflinePlayer(UUID)
	 */
	public BukkitPlayer(UUID uuid) {
		this(Bukkit.getOfflinePlayer(uuid));
	}
	
	/**
	 * Construct a new instance using the given {@link OfflinePlayer} as the handle. If the handle is {@code null}, the both the
	 * name and UUID of this player will also be {@code null}.
	 * 
	 * @param player
	 *            The player
	 */
	public BukkitPlayer(OfflinePlayer player) {
		this(player != null ? player.getName() : null, player != null ? player.getUniqueId() : null);
	}
	
	/**
	 * Construct a new instance with the given name and UUID. The name does not have the be the player's actual name, it is only used
	 * in {@link #getName()}
	 * 
	 * @param name
	 *            The player's name
	 * @param uuid
	 *            The player's uuid
	 */
	public BukkitPlayer(String name, UUID uuid) {
		_name = name;
		_id = uuid;
	}
	
	/**
	 * Get the plyer's name. The handle is not retrieved.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Get the player's UUID object.
	 * 
	 * @return The UUID.
	 */
	public UUID getUUID() {
		return _id;
	}
	
	/**
	 * Get the player's UUID as a string.
	 * 
	 * @return The UUID string.
	 */
	public String getUniqueId() {
		if (_id == null)
			return null;
		
		return _id.toString();
	}
	
	/**
	 * Get the location of the player or {@code null} if they aren't online
	 * 
	 * @return The location
	 */
	public Location getLocation() {
		return getHandleAnd((h) -> h.getLocation());
	}
	
	/**
	 * Get the player, or null if the player is not online.
	 * 
	 * @return The player.
	 */
	public Player getHandle() {
		return Bukkit.getPlayer(_id);
	}
	
	public <R> R getHandleAnd(Function<Player, R> f) {
		return getHandleAnd(null, f);
	}
	
	public <R> R getHandleAnd(R def, Function<Player, R> f) {
		Player handle = getHandle();
		if (handle == null)
			return def;
		
		return f.apply(handle);
	}
	
	public boolean getHandleThen(Consumer<Player> consumer) {
		Player handle = getHandle();
		if (handle == null)
			return false;
		consumer.accept(handle);
		return true;
	}
	
	public World getWorld() {
		return getHandleAnd(h -> h.getWorld());
	}
	
	public void teleport(Location location) {
		getHandleAnd((handle) -> handle.teleport(location));
	}
	
	public void teleportToTarget() {
		teleport(BukkitWorld.getSafePosition(getTargetLocation()));
	}
	
	public void teleportToTarget(boolean ignoreEntities) {
		teleport(BukkitWorld.getSafePosition(getTargetLocation(ignoreEntities)));
	}
	
	public void teleportToTarget(boolean ignoreEntities, int range) {
		teleport(BukkitWorld.getSafePosition(getTargetLocation(ignoreEntities, range)));
	}
	
	public void sendMessage(String message) {
		sendMessage(message, BukkitMessages.DEFAULT_COLOR_CHAR);
	}
	
	public void sendMessage(String message, char colorChar) {
		getHandleThen((h) -> h.sendMessage(BukkitMessages.format(colorChar, message)));
	}
	
	public Entity getTargetEntity() {
		return getTargetEntity(DEF_RANGE);
	}
	
	public Entity getTargetEntity(int range) {
		Object target = getLastTwoTargets(false, true, range)[0];
		if (target == null || !(target instanceof Entity))
			return null;
		
		return (Entity) target;
	}
	
	public Object[] getLastTwoTargets(boolean includeBlocks, boolean includeEntities) {
		return getLastTwoTargets(includeBlocks, includeEntities, DEF_RANGE);
	}
	
	//first index is last in line of sight
	public Object[] getLastTwoTargets(boolean includeBlocks, boolean includeEntities, int range) {
		if (!(includeBlocks || includeEntities))
			return null;
		
		List<Entity> entities = null;
		if (includeEntities)
			entities = getHandleAnd((h) -> h.getNearbyEntities(range, range, range));
		
		BlockIterator it = new BlockIterator(getHandle(), range);
		Object[] targets = new Object[2];
		while (it.hasNext()) {
			Block b = it.next();
			if (!b.getChunk().isLoaded())
				break;
			
			Object target = includeBlocks ? b : null;
			if (entities != null && !entities.isEmpty())
				for (Entity e : entities) {
					Location loc = e.getLocation();
					if (loc.getBlockX() == b.getX() &&
						loc.getBlockY() == b.getY() &&
						loc.getBlockZ() == b.getZ())
						target = e;
				}
			
			if (target != null) {
				targets[1] = targets[0];
				targets[0] = target;
				if (includeBlocks && b.getType() != Material.AIR)
					break;
			}
		}
		
		return targets;
	}
	
	public Block getTargetBlock() {
		return getTargetBlock(DEF_RANGE);
	}
	
	public Block getTargetBlock(int range) {
		return (Block) getLastTwoTargets(true, false, range)[0];
	}
	
	public Location getTargetLocation() {
		return getTargetLocation(false);
	}
	
	public Location getTargetLocation(boolean ignoreEntities) {
		return getTargetLocation(ignoreEntities, DEF_RANGE);
	}
	
	public Location getTargetLocation(boolean ignoreEntities, int range) {
		Entity targetEntity = ignoreEntities ? null : getTargetEntity(range);
		if (targetEntity == null) {
			Block targetBlock = getTargetBlock(range);
			if (targetBlock == null)
				return null;
			return targetBlock.getLocation();
		}
		return targetEntity.getLocation();
	}
	
	public BlockFace getTargetFace() {
		return getTargetFace(DEF_RANGE);
	}
	
	public BlockFace getTargetFace(int range) {
		List<Block> blocks = getHandleAnd((h) -> h.getLastTwoTargetBlocks((Set<Material>) null, range));
		return blocks.get(0).getFace(blocks.get(1));
	}
	
	public Block getFirstAirBlockAboveTarget() {
		return getFirstAirBlockAboveTarget(DEF_RANGE);
	}
	
	public Block getFirstAirBlockAboveTarget(int range) {
		Block block = getTargetBlock(range);
		World world = block.getWorld();
		int x = block.getX();
		int z = block.getZ();
		int max = block.getWorld().getMaxHeight();
		if (block.getY() == max)
			return null;
		
		for (int y = block.getY() + 1; y <= max; y++) {
			Block b = world.getBlockAt(x, y, z);
			if (b.getType() == Material.AIR)
				return b;
		}
		return null;
	}
	
}
