package info.malignantshadow.api.bukkit.plugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ShadowPlugin extends JavaPlugin {

	public abstract void onStart();

	public abstract void onEnd();

	@Override
	public void onEnable() {
		onStart();
	}

	@Override
	public void onDisable() {
		onEnd();
	}

	public void registerEvents(Listener listener) {
		ShadowAPI.registerEvents(this, listener);
	}

	public BukkitRunnable createRunnable(Runnable r) {
		return new BukkitRunnable() {

			@Override
			public void run() {
				r.run();
			}

		};
	}

	public BukkitRunnable runTask(Runnable r) {
		BukkitRunnable br = createRunnable(r);
		br.runTask(this);
		return br;
	}

	public BukkitRunnable runTaskLater(long delay, Runnable r) {
		BukkitRunnable br = createRunnable(r);
		br.runTaskLater(this, delay);
		return br;
	}

	public BukkitRunnable runTaskTimer(long delay, long period, Runnable r) {
		BukkitRunnable br = createRunnable(r);
		br.runTaskTimer(this, delay, period);
		return br;
	}

	public BukkitRunnable runTaskAsync(Runnable r) {
		BukkitRunnable br = createRunnable(r);
		br.runTaskAsynchronously(this);
		return br;
	}

	public BukkitRunnable runTaskLaterAsync(long delay, Runnable r) {
		BukkitRunnable br = createRunnable(r);
		br.runTaskLaterAsynchronously(this, delay);
		return br;
	}

	public BukkitRunnable runTaskTimerAsync(long delay, long period, Runnable r) {
		BukkitRunnable br = createRunnable(r);
		br.runTaskTimerAsynchronously(this, delay, period);
		return br;
	}

}
