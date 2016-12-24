package info.malignantshadow.api.bukkit.selectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import info.malignantshadow.api.util.ListUtil;
import info.malignantshadow.api.util.StringUtil;
import info.malignantshadow.api.util.arguments.ArgumentTypes;
import info.malignantshadow.api.util.selectors.Selector;

public class BukkitSelector {
	
	// @me and @l are not part of Minecraft
	// @me - select yourself
	// @l - only living entities
	private static final String[] ALLOWED_NAMES = { "@a", "@e", "@r", "@l", "@p", "@me" };
	
	private Selector _selector;
	private boolean _random;
	private Location _ref;
	
	//default - tag and scoreboard not supported
	private Number _x, _y, _z, _dx, _dy, _dz, _r, _rm, _rx, _ry, _rxm, _rym, _l, _lm;
	private Integer _c;
	private List<EntityType> _allowedTypes;
	private List<String> _allowedNames;
	
	//extra
	private Boolean _alive; //null = any
	private List<String> _perms;
	private boolean _valid;
	
	public BukkitSelector(Selector selector) {
		_valid = true;
		if (!selectorAllowed(selector)) {
			_valid = false;
			return;
		}
		_selector = selector;
		
		_random = selector.nameIs("@r");
		
		_x = getNumber("x"); //location to look
		_y = getNumber("y");
		_z = getNumber("z");
		
		_dx = getNumber("dx"); //bounding box: deltas from reference location
		_dy = getNumber("dy");
		_dz = getNumber("dz");
		
		_r = getNumber("r"); //radius from reference location
		_rm = getNumber("rm"); //min radius from reference location
		
		/*
		 * !!NB!!
		 * 
		 * Normally in games, I would assume that pitch/yaw/roll would correspond to the x/y/z planes respectively:
		 * http://theboredengineers.com/WordPress3/wp-content/uploads/2012/05/PitchRollYaw.png
		 * In other words, pitch is up-down, yaw is side-to-side, and roll would be the same as the entire screen rotating ("do a barrel roll")
		 * 
		 * However, Minecraft/Bukkit do it differently (for some reason):
		 * https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Location.html#Location(org.bukkit.World,%20double,%20double,%20double,%20float,%20float)
		 * Where yaw is up-down, and pitch is side-to-side (roll is unused)
		 * *sigh*
		 * 
		 * Fun fact - Minecraft had a similar quirk in Classic 0.30
		 * Instead of coordinates being read as x,y,z (as they are now) they were were read as x,z,y
		 */
		
		_rx = getNumber("rx");   //rotation along x-axis: yaw
		_ry = getNumber("ry");   //rotation along y-axis: pitch
		_rxm = getNumber("rxm"); //max yaw
		_rym = getNumber("rym"); //max pitch
		
		_l = getNumber("l");
		_lm = getNumber("lm");
		
		_c = selector.getOne("c", ArgumentTypes.INT_LENIENT);
		
		_allowedTypes = StringUtil.lenientEnumSearch(EntityType.values(), selector.getInputFor("type"));
		
		_allowedNames = selector.getAll("name");
		
		_alive = selector.getOne("alive", ArgumentTypes.BOOLEAN_LENIENT(true));
		
		_perms = selector.getAll("perm", ArgumentTypes.STRING);
	}
	
	public boolean isValid() {
		return _valid;
	}
	
	protected boolean selectorAllowed(Selector selector) {
		if (selector == null)
			return false;
		
		for (String s : ALLOWED_NAMES) {
			if (selector.getName().equals(s))
				return true;
		}
		
		return false;
	}
	
	public Selector getSelector() {
		return _selector;
	}
	
	private Number getNumber(String name) {
		return _selector.getOne(name, ArgumentTypes.NUMBER);
	}
	
	public World getReferenceWorld(CommandSender sender) {
		if (sender == null || !(sender instanceof Entity))
			return Bukkit.getWorlds().get(0);
		
		return ((Entity) sender).getLocation().getWorld();
	}
	
	public void setReferencePoint(Location location) {
		_ref = location;
	}
	
	public Location getReferencePoint() {
		return _ref;
	}
	
	public Location getReferencePoint(CommandSender sender) {
		Location ref;
		if (_ref == null)
			if (sender == null || !(sender instanceof Entity))
				ref = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
			else
				ref = ((Entity) sender).getLocation();
		else
			ref = _ref.clone();
		
		if (_x != null)
			ref.setX(_x.doubleValue());
		if (_y != null)
			ref.setY(_y.doubleValue());
		if (_z != null)
			ref.setY(_z.doubleValue());
		
		return ref;
	}
	
	public List<Entity> select(CommandSender sender) {
		if (!_valid)
			return null;
		
		if (_selector.nameIs("@me") && sender instanceof Entity)
			return Arrays.asList((Entity) sender);
		
		World ref = getReferenceWorld(sender);
		List<Entity> entities = ref.getEntities();
		List<Entity> players = new ArrayList<Entity>(Bukkit.getOnlinePlayers());
		if (_selector.nameIs("@p") || (_selector.nameIs("@r") && !_allowedTypes.isEmpty())) {
			if (!_selector.isSet("c"))
				_selector.add("c", "1");
			return select(players, sender);
		}
		
		if (_selector.nameIs("@l"))
			return select(ListUtil.slice(entities, e -> e != null && e.getType() != EntityType.ARMOR_STAND && e.getType().isAlive()), sender);
		
		if (_selector.nameIs("@a"))
			return select(players, sender);
		
		return select(entities, sender);
	}
	
	public static String entityName(Entity e) {
		return e instanceof Player ? ((Player) e).getName() : e.getCustomName();
	}
	
	public List<Entity> select(List<Entity> entities, CommandSender sender) {
		List<Entity> selected = new ArrayList<Entity>();
		if (entities == null || entities.isEmpty())
			return selected;
		
		Location ref = getReferencePoint(sender);
		
		for (Entity e : entities) {
			if (e == null)
				continue;
			
			//test location
			Location location = new Location(null, 0, 0, 0);
			e.getLocation(location);
			location.setWorld(ref.getWorld());
			if (_r != null || _rm != null) {
				if ((_r != null && location.distanceSquared(ref) > _r.doubleValue() * _r.doubleValue()) ||
					(_rm != null && location.distance(ref) < _rm.doubleValue() * _rm.doubleValue()))
					continue;
			} else if (_dx != null || _dy != null || _dz != null) {
				if ((_dx != null && (location.getX() < ref.getX() || location.getX() > ref.getX() + _dx.doubleValue())) ||
					(_dy != null && (location.getY() < ref.getY() || location.getY() > ref.getY() + _dy.doubleValue())) ||
					(_dz != null && (location.getZ() < ref.getZ() || location.getZ() > ref.getZ() + _dz.doubleValue())))
					continue;
			} else if ((_x != null && location.getBlockX() != _x.intValue()) ||
				(_y != null && location.getBlockY() != _y.intValue()) ||
				(_z != null && location.getBlockZ() != _z.intValue()))
				continue;
			
			//test type
			if (_allowedTypes != null && !_allowedTypes.isEmpty() && !ListUtil.contains(_allowedTypes, (type) -> type == e.getType()))
				continue;
			
			//test name
			String name = entityName(e);
			if (_allowedNames != null && !_allowedNames.isEmpty() && !ListUtil.contains(_allowedNames, n -> StringUtil.lenientMatch(n, name)))
				continue;
			
			//rotation
			if ((_rx != null && location.getYaw() > _rx.doubleValue()) || (_rxm != null && location.getYaw() < _rxm.doubleValue()) ||
				(_ry != null && location.getPitch() > _ry.doubleValue()) || (_rym != null && location.getPitch() < _rym.doubleValue()))
				continue;
			
			//test if alive
			if (_alive != null && (!(e instanceof LivingEntity) || (((LivingEntity) e).getHealth() > 0) != _alive))
				continue;
			
			//experience level
			if ((_l != null && _lm != null) && e instanceof Player) {
				Player p = (Player) e;
				if ((_l != null && p.getLevel() > _l.intValue()) || (_lm != null && p.getLevel() < _lm.intValue()))
					continue;
			}
			
			//permissions
			if ((_perms != null) && e instanceof Permissible) {
				Permissible p = e;
				if (!ListUtil.contains(_perms, (perm) -> p.hasPermission(perm)))
					continue;
			}
			
			selected.add(e);
			
		}
		
		if (_c != null)
			selected = restrictSize(selected);
		
		return selected;
	}
	
	public <T> List<T> restrictSize(List<T> list) {
		return (_random ? new ListUtil.RandomSizeRestricter() : new ListUtil.DefaultSizeRestricter()).restrictSize(list, _c);
	}
	
}
