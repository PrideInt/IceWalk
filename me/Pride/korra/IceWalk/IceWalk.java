package me.Pride.korra.IceWalk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.IceAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ActionBar;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;

import net.md_5.bungee.api.ChatColor;

public class IceWalk extends IceAbility implements AddonAbility {
	
	private static String path = "ExtraAbilities.Prride.IceWalk.";
	FileConfiguration config = ConfigManager.getConfig();
	
	private ArrayList<Block> bLocs = new ArrayList<Block>();
	
	private long cooldown;
	private long duration;
	private boolean regen;
	private long regenTime;
	
	private long time;
	public boolean toggled;
	
	private Material feetMaterial;
	private TempBlock ice;
	private TempBlock ice1;

	public IceWalk(Player player) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		
		if (bPlayer.isOnCooldown(this)) {
			return;
		}
		
		if (player.getLocation().getBlock().getType() == Material.WATER) {
			return;
		}
		
		cooldown = config.getLong(path + "Cooldown");
		duration = config.getLong(path + "Duration");
		regenTime = config.getLong(path + "RegenTime");
		regen = config.getBoolean(path + "Regen");
		
		time = System.currentTimeMillis();
		
		if (toggled) {
			ActionBar.sendActionBar(ChatColor.AQUA + "" + ChatColor.BOLD + "UNTOGGLED", player);
			
			toggled = false;
			
			bPlayer.addCooldown(this);
		} else {
			ActionBar.sendActionBar(ChatColor.AQUA + "" + ChatColor.BOLD + "TOGGLED", player);
			
			toggled = true;
			
			start();
		}
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public String getName() {
		return "IceWalk";
	}

	@Override
	public boolean isHarmlessAbility() {
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public void progress() {
		feetMaterial = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
		
		if (feetMaterial == Material.WATER) {
			ParticleEffect.SNOW_SHOVEL.display(player.getLocation(), 3);
			
			List<Location> area = GeneralMethods.getCircle(player.getLocation(), 2, 3, false, true, 0);
			for (Location loc : area) {
				
				bLocs.add(loc.getBlock());
				
				for (Iterator<Block> iterator = bLocs.iterator(); iterator.hasNext();) {
					
					Block block = iterator.next();
					
					if (block.getType() != Material.WATER) {
						iterator.remove();
						
					} else {
					
						if (regen) {
							
							ice = new TempBlock(block, Material.ICE);
							ice.setRevertTime(regenTime);
							
							ice1 = new TempBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN), Material.ICE);
							ice1.setRevertTime(regenTime);
							
						} else {
							block.setType(Material.ICE);
							
							player.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.ICE);
						}
					}
				}
			}
		}
		
		if (System.currentTimeMillis() > time + duration) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
	}

	@Override
	public String getDescription() {
		return Element.WATER.getColor() + "Although waterbenders may have a difficult time walking on water, many waterbenders are able "
				+ "to generate ice on the bottom of their feet in order to effectively walk on water.";
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.GOLD + "Left click to toggle.";
	}

	@Override
	public String getAuthor() {
		return Element.WATER.getColor() + "" + ChatColor.UNDERLINE + "Prride & Shookified";
	}

	@Override
	public String getVersion() {
		return Element.WATER.getColor() + "" + ChatColor.UNDERLINE + 
				"VERSION 1";
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new IceWalkListener(), ProjectKorra.plugin);
		ProjectKorra.log.info(getName() + " " + getVersion() + " by " + getAuthor() + " loaded! ");
		
		ConfigManager.getConfig().addDefault(path + "Cooldown", 7000);
		ConfigManager.getConfig().addDefault(path + "Duration", 15000);
		ConfigManager.getConfig().addDefault(path + "RegenTime", 2000);
		ConfigManager.getConfig().addDefault(path + "Regen", false);
		ConfigManager.defaultConfig.save();
	}

	@Override
	public void stop() {
		ProjectKorra.log.info(getName() + " " + getVersion() + " by " + getAuthor() + " stopped! ");
		super.remove();
	}

}
