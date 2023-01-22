package machinegun.max.me.machinegun;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MachineGun extends JavaPlugin implements Listener {

    private BukkitTask task;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.STICK) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (!player.hasPermission("firestick.use")) return;
        if (task == null) {
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.getInventory().getItemInMainHand().getType() != Material.STICK) {
                        task.cancel();
                        task = null;
                        return;
                    }
                    Arrow arrow = player.launchProjectile(Arrow.class);
                    arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                    Bukkit.getScheduler().runTaskLater(MachineGun.this, arrow::remove, 100);
                }
            }.runTaskTimer(this, 0, 3);
        }
    }
}
