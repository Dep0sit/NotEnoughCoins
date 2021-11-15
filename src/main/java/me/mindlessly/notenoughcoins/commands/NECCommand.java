package me.mindlessly.notenoughcoins.commands;

import gg.essential.api.utils.GuiUtil;
import me.mindlessly.notenoughcoins.commands.subcommands.Subcommand;
import me.mindlessly.notenoughcoins.utils.Config;
import me.mindlessly.notenoughcoins.utils.Reference;
import me.mindlessly.notenoughcoins.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class NECCommand extends CommandBase {
    private final Subcommand[] subcommands;

    public NECCommand(Subcommand[] subcommands) {
        this.subcommands = subcommands;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("notenoughcoins", "notenoughcoin");
    }

    @Override
    public String getCommandName() {
        return "nec";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/nec <subcommand> <arguments>";
    }

    public void sendHelp(ICommandSender sender) {
        List<String> commandUsages = new LinkedList<>();
        for (Subcommand subcommand : this.subcommands) {
            commandUsages.add(EnumChatFormatting.AQUA + "/nec " + subcommand.getCommandName() + " " + subcommand.getCommandUsage(sender));
        }
        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GOLD + "NEC " + EnumChatFormatting.GREEN + Reference.VERSION + "\n" +
                        String.join("\n", commandUsages)
        ));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            GuiUtil.open(Objects.requireNonNull(Config.INSTANCE.gui()));
            return;
        }
        for (Subcommand subcommand : this.subcommands) {
            if (Objects.equals(args[0], subcommand.getCommandName())) {
                if (!subcommand.processCommand(sender, Arrays.copyOfRange(args, 1, args.length))) {
                    // processCommand returned false
                    Utils.sendMessageWithPrefix("&cFailed to execute command, command usage: /nec " + subcommand.getCommandName() + " " + subcommand.getCommandUsage(sender),
                            sender);
                }
                return;
            }
        }
        Utils.sendMessageWithPrefix(EnumChatFormatting.RED + "The subcommand wasn't found, please refer to the help message below for the list of subcommands",
                sender);
        sendHelp(sender);
    }
    // This causes crash upon TAB for some reason
    //@Override
    //public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    //    List<String> possibilities = new LinkedList<>();
    //    for (Subcommand subcommand : subcommands) {
    //        possibilities.add(subcommand.getCommandName());
    //    }
    //    if (args.length == 1) {
    //        return getListOfStringsMatchingLastWord(args, possibilities);
    //    }
    //    return null;
    //}
}
