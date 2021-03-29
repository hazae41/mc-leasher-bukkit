package hazae41.leasher;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

import static org.bukkit.event.entity.EntityUnleashEvent.UnleashReason;

public class Plugin extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    super.onEnable();

    getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler(priority = EventPriority.NORMAL)
  public void onUnleash(EntityUnleashEvent e){
    if(e.getReason() != UnleashReason.DISTANCE) return;
    if(!(e.getEntity() instanceof LivingEntity)) return;
    LivingEntity living = (LivingEntity) e.getEntity();
    Entity holder = living.getLeashHolder();

    getServer().getScheduler().runTask(this, task -> {
      Optional<Item> lead = living.getNearbyEntities(15.0, 15.0, 15.0).stream()
              .filter(entity -> entity instanceof Item)
              .map(entity -> (Item) entity)
              .filter(item -> item.getItemStack().getType() == Material.LEAD)
              .findFirst();

      if(!lead.isPresent()) return;
      lead.get().remove();

      living.teleport(holder);
      living.setLeashHolder(holder);
    });
  }
}
