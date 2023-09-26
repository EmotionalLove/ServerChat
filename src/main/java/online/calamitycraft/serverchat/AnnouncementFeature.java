package online.calamitycraft.serverchat;

import gaming.femboy.tinnitus.Listener;
import gaming.femboy.tinnitus.Reactor;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.entity.player.EntityPlayerMP;
import online.calamitycraft.serverchat.event.PlayerJoinServerEvent;
import online.calamitycraft.serverchat.event.PlayerTryMoveEvent;
import online.calamitycraft.serverchat.util.SpawnUtil;
import online.calamitycraft.serverchat.util.Threads;
import online.calamitycraft.serverchat.util.WhisperUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AnnouncementFeature implements Listener {

    public AnnouncementFeature() {

    }
    private final Reactor<?> onJoin = new Reactor<PlayerJoinServerEvent>(event -> {
        Future<String> stringFuture = getAnnouncementAsync("");
        new Thread(() -> {
            while (!stringFuture.isDone());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
            }
            try {
                String a = stringFuture.get();
                if (a == null || a.isEmpty()) return;
                if (event.getPlayer() != null) {
                    event.getPlayer().addChatMessage(TextFormatting.ORANGE + stringFuture.get().trim());
                }
            } catch (InterruptedException | ExecutionException e) {
                //
            }
        }).start();
    }).register(ServerChatMod.getEventManager(), this);

    private Future<String> getAnnouncementAsync(String def) {
        return Threads.announcementExecutor.submit(() -> ServerChatMod.config.getAnnouncement(""));
    }

}
