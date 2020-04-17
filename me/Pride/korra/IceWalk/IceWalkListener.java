package me.Pride.korra.IceWalk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;

public class IceWalkListener implements Listener {
	
	@EventHandler
	public void onSwing(PlayerAnimationEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		
		if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility("IceWalk")) && CoreAbility.getAbility(event.getPlayer(), IceWalk.class) == null) {
			
			new IceWalk(player);
		}
	}

}
