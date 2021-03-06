package com.tinytimrob.ppse.napbot.commands;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.tinytimrob.common.CommonUtils;
import com.tinytimrob.ppse.napbot.CommonPolyStuff;
import com.tinytimrob.ppse.napbot.NapBotDb;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandMSetTimestamp implements ICommand
{
	@Override
	public String[] getCommandName()
	{
		return new String[] { "msettimestamp" };
	}

	@Override
	public boolean hasPermission(User user)
	{
		return CommonPolyStuff.isUserModerator(user);
	}

	@Override
	public boolean execute(User moderator, TextChannel channel, String command, List<String> parameters, Message message) throws Exception
	{
		if (parameters.size() != 3)
		{
			return false;
		}

		Member matchedMember = CommonPolyStuff.findMemberMatch(channel, parameters.get(0));

		if (matchedMember == null)
		{
			return true;
		}

		String timestring = parameters.get(1) + " " + parameters.get(2);
		Timestamp timestamp;

		try
		{
			Date parsedDate = CommonUtils.dateFormatter.parse(timestring);

			// Fucks with SQLite formatting.. I don't think we have any users that old anyway!
			if (parsedDate.getTime() < 0)
			{
				throw new Exception();
			}

			timestamp = new Timestamp(parsedDate.getTime());
		}
		catch (Throwable t)
		{
			channel.sendMessage(moderator.getAsMention() + " Bad timestamp: " + timestring).complete();
			return true;
		}

		User user = matchedMember.getUser();
		NapBotDb.setNapchartTimestamp(user, timestamp);

		channel.sendMessage(moderator.getAsMention() + " The sleep schedule timestamp for **" + matchedMember.getEffectiveName() + "** has been set to " + CommonUtils.convertTimestamp(timestamp) + " UTC.").complete();
		return true;
	}

	@Override
	public String getCommandHelpUsage()
	{
		return "msettimestamp [username] [YYYY-MM-DD HH:MM:SS]";
	}

	@Override
	public String getCommandHelpDescription()
	{
		return "set someone's napchart start timestamp";
	}
}
