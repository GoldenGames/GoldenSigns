package me.mani.goldensigns;

import org.bukkit.Bukkit;

public class UpdatingScheduler {

	private GoldenSigns pl;
	
	public UpdatingScheduler(GoldenSigns pl) {
		this.pl = pl;
	}
	
	public void start() {
		startServerInfoUpdater();
		startSignUpdater();
	}
	
	private void startServerInfoUpdater() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(pl, new Runnable() {
			
			@Override
			public void run() {
				for (ServerSign sign : pl.getAllServerSigns()) {
					sign.updateData();
				}
			}
			
		}, 40L, 5L);
	}
	
	private void startSignUpdater() {
		Bukkit.getScheduler().runTaskTimer(pl, new Runnable() {
			
			@Override
			public void run() {
				for (ServerSign sign : pl.getAllServerSigns()) {
					sign.updateSign();
				}
			}
			
		}, 41L, 5L);
	}
	
}
