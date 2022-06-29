package me.soarz.server

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandManager
import net.minestom.server.command.CommandSender
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.utils.callback.CommandCallback

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // Initialization
            val minecraftServer: MinecraftServer = MinecraftServer.init()
            val instanceManager: InstanceManager = MinecraftServer.getInstanceManager()
            val commandManager: CommandManager = MinecraftServer.getCommandManager()

            commandManager.unknownCommandCallback = CommandCallback { sender: CommandSender, command: String ->
                sender.sendMessage(Component.text("Unknown '$command' command.", NamedTextColor.RED))
            }

            val instanceContainer: InstanceContainer = instanceManager.createInstanceContainer()

            instanceContainer.setGenerator { unit: GenerationUnit ->
                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK)
            }

            val globalEventHandler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()
            globalEventHandler.addListener(PlayerLoginEvent::class.java) { event: PlayerLoginEvent ->
                val player: Player = event.player
                event.setSpawningInstance(instanceContainer)
                player.respawnPoint = Pos(0.0, 42.0, 0.0)
            }

            // Start the server on port 25565
            minecraftServer.start("0.0.0.0", 25565)
        }
    }
}
