package com.blackgeckogames.server.mod.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.blackgeckogames.server.mod.BlackGeckoServer;
import com.blackgeckogames.server.mod.gamemode.skybattle.EnumTeam;
import com.blackgeckogames.server.mod.gamemode.skybattle.SkyBattle;

public class SkyBattleCommand implements ICommand {
	private List aliases;

	public SkyBattleCommand() {
		this.aliases = new ArrayList();
		this.aliases.add("sb");
		this.aliases.add("skybattle");
	}

	@Override
	public String getName() {
		return "skyBattle";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/skyBattle <stop:start:create>";
	}

	@Override
	public void execute(ICommandSender icommandsender, String[] args) {
		if (args.length == 0) {
			icommandsender.addChatMessage(new ChatComponentText("/skyBattle <stop:start:create>"));
			return;
		}

		if (args[0].equals("start")) {
			if (args.length > 1) {
				if (args.length == 2) {
					int number = 0;

					try {
						number = Integer.parseInt(args[1]);
					} catch (NumberFormatException nfe) {
						// bad data - set to sentinel
					}
					if (number != 0) {

						if (BlackGeckoServer.hasGame((number + BlackGeckoServer.firstSkyBattleServer - 1)) && BlackGeckoServer.getGame(number + BlackGeckoServer.firstSkyBattleServer - 1) instanceof SkyBattle) {
							BlackGeckoServer.getGame(number + BlackGeckoServer.firstSkyBattleServer - 1).countdown();
						} else {
							icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Unable to find skybattle in dim " + (number + BlackGeckoServer.firstSkyBattleServer - 1)));
						}
					} else {
						icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <start> [number]"));
					}
				} else {
					icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <start> [number]"));
				}
			} else {
				if (icommandsender instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) icommandsender;
					if (BlackGeckoServer.hasGame((player.dimension)) && BlackGeckoServer.getGame(player.dimension) instanceof SkyBattle) {
						BlackGeckoServer.getGame(player.dimension).countdown();
						icommandsender.addChatMessage(new ChatComponentText("Started the game."));
					} else {
						icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <start> [number]"));

					}		
				} else {
					icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <start> [number]"));
				}
			}
		}

		else if (args[0].equals("stop")) {
			if (args.length > 1) {
				if (args.length == 2) {
					int number = 0;

					try {
						number = Integer.parseInt(args[1]);
					} catch (NumberFormatException nfe) {
						// bad data - set to sentinel
					}
					if (number != 0) {

						if (BlackGeckoServer.hasGame((number + BlackGeckoServer.firstSkyBattleServer - 1)) && BlackGeckoServer.getGame(number + BlackGeckoServer.firstSkyBattleServer - 1) instanceof SkyBattle) {
							BlackGeckoServer.getGame(number + BlackGeckoServer.firstSkyBattleServer - 1).stop();
						} else {
							icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Unable to find skybattle in dim " + (number + BlackGeckoServer.firstSkyBattleServer - 1)));
						}
					} else {
						icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <stop> [number]"));
					}
				} else {
					icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <stop> [number]"));
				}
			} else {
				if (icommandsender instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) icommandsender;
					if (BlackGeckoServer.hasGame((player.dimension)) && BlackGeckoServer.getGame(player.dimension) instanceof SkyBattle) {
						BlackGeckoServer.getGame(player.dimension).stop();
					}
				} else {
					icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <stop> [number]"));
				}
			}
		}

		else if (args[0].equals("create")) {
			if (icommandsender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) icommandsender;
				if (args.length == 1) {
					icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <new:set:save>"));
				} else if (args.length == 2) {
					if (args[1].equals("new")) {
						if (BlackGeckoServer.hasGame(player.dimension)) {
							icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "There is already a sky battle in this dim."));
						} else {
							BlackGeckoServer.gameServer.put(player.dimension, new SkyBattle(player.dimension));
							BlackGeckoServer.getGame(player.dimension).setCreateMode(true);
							icommandsender.addChatMessage(new ChatComponentText("Created."));
						}
					} else if (args[1].equals("save")) {
						if (BlackGeckoServer.hasGame(player.dimension)) {
							if (BlackGeckoServer.getGame(player.dimension).isCreateMode()) {
								boolean ready = true;
								SkyBattle skyBattle = (SkyBattle) BlackGeckoServer.getGame(player.dimension);

								if (skyBattle.mapName.equals("")) {
									icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Set a name first."));
									ready = false;
								}
								if (skyBattle.teamSize == 0) {
									icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Set playersPerTeam first."));
									ready = false;
								}
								if (skyBattle.lobbySpawnPos == null) {
									icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Set a lobby spawn pos first."));
									ready = false;
								}
								if (skyBattle.teamSpawnPos.size() < 2) {
									icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Set team spawn pos first."));
									ready = false;
								}

								if (skyBattle.ironSpawnPos.size() < 1) {
									icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Set an iron spawn pos first."));
									ready = false;
								}
								if (skyBattle.goldSpawnPos.size() < 1) {
									icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Set an gold spawn pos first."));
									ready = false;
								}
								if (skyBattle.diamondSpawnPos.size() < 1) {
									icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Set an diamond spawn pos first."));
									ready = false;
								}
								if(skyBattle.beaconPos.size()<skyBattle.teamSpawnPos.size()){
									icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Please set the beacon pos first."));
									ready = false;
								}

								if (ready) {
									BlackGeckoServer.getGame(player.dimension).writeToJson();
									icommandsender.addChatMessage(new ChatComponentText("Saved."));
							        BlackGeckoServer.gameServer.remove(player.dimension);

								}
							} else {
								icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "This sky battle is not in create-mode."));
							}
						}
					}
				}
				if (args.length>1) {
					if(args[1].equals("set")) {
					if (!BlackGeckoServer.hasGame(player.dimension)) {
						icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Please use "+EnumChatFormatting.ITALIC+"/skyBattle create new" + EnumChatFormatting.RESET + EnumChatFormatting.RED + " first."));
					} else {
						if (BlackGeckoServer.getGame(player.dimension).isCreateMode()) {
							SkyBattle skyBattle = (SkyBattle) BlackGeckoServer.getGame(player.dimension);

							if (args.length == 2) {
								icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <name:teamSize:lobby:beaconAdd:beaconClearteamSpawnAdd:teamSpawnClear:ironAdd:ironClear:goldAdd:goldClear:diaAdd:diaClear>"));
							} else if(args.length>=3) {
								if (args[2].equals("name")) {
									if (args.length == 4) {
										skyBattle.mapName = args[3];
										icommandsender.addChatMessage(new ChatComponentText("Named: " + args[3]));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <name> [NAME]"));
									}
								} else if (args[2].equals("teamSize")) {
									if (args.length == 4) {
										int number = 0;
										try {
											number = Integer.parseInt(args[3]);
										} catch (NumberFormatException nfe) {
										}

										if (number != 0) {
											skyBattle.teamSize = number;
											icommandsender.addChatMessage(new ChatComponentText("Set team size to " + number));
										} else {
											icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <teamSize> [NUMBER]"));
										}
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <teamSize> [NUMBER]"));
									}
								} else if (args[2].equals("lobby")) {
									if (args.length == 3) {
										skyBattle.lobbySpawnPos = player.getPositionVector();
										icommandsender.addChatMessage(new ChatComponentText("Created lobby spawn location."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <lobby>"));
									}
								} else if (args[2].equals("teamSpawnAdd")) {
									if (args.length == 3) {
										skyBattle.teamSpawnPos.add(player.getPositionVector());
										icommandsender.addChatMessage(new ChatComponentText("Added Team Spawn for team for team " + EnumTeam.values()[(skyBattle.teamSpawnPos.size() - 1)] + "."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <teamSpawnAdd>"));
									}
								} else if (args[2].equals("teamSpawnClear")) {
									if (args.length == 3) {
										skyBattle.teamSpawnPos.clear();
										icommandsender.addChatMessage(new ChatComponentText("Removed all team spawn locations."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <teamSpawnClear>"));
									}
								} else if (args[2].equals("ironAdd")) {
									if (args.length == 3) {
										skyBattle.ironSpawnPos.add(player.getPositionVector());
										icommandsender.addChatMessage(new ChatComponentText("Added iron spawn location."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <ironAdd>"));
									}
								} else if (args[2].equals("ironClear")) {
									if (args.length == 3) {
										skyBattle.ironSpawnPos.clear();
										icommandsender.addChatMessage(new ChatComponentText("Removed all iron spawn locations."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <ironClear>"));
									}
								} else if (args[2].equals("goldAdd")) {
									if (args.length == 3) {
										skyBattle.goldSpawnPos.add(player.getPositionVector());
										icommandsender.addChatMessage(new ChatComponentText("Added gold spawn location."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <goldAdd>"));
									}
								} else if (args[2].equals("goldClear")) {
									if (args.length == 3) {
										skyBattle.goldSpawnPos.clear();
										icommandsender.addChatMessage(new ChatComponentText("Removed all gold spawn locations."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <goldClear>"));
									}
								} else if (args[2].equals("diaAdd")) {
									if (args.length == 3) {
										skyBattle.diamondSpawnPos.add(player.getPositionVector());
										icommandsender.addChatMessage(new ChatComponentText("Added diamond spawn location."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <diaAdd>"));
									}
								} else if (args[2].equals("diaClear")) {
									if (args.length == 3) {
										skyBattle.diamondSpawnPos.clear();
										icommandsender.addChatMessage(new ChatComponentText("Removed all diamon spawn locations."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <diaClear>"));
									}
								} else if (args[2].equals("beaconAdd")) {
									if (args.length == 3) {
										skyBattle.beaconPos.add(player.getPositionVector());
										icommandsender.addChatMessage(new ChatComponentText("Added beacon location."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <beaconAdd>"));
									}
								} else if (args[2].equals("beaconClear")) {
									if (args.length == 3) {
										skyBattle.beaconPos.clear();
										icommandsender.addChatMessage(new ChatComponentText("Removed all beacon locations."));
									} else {
										icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/skyBattle <create> <set> <beaconClear>"));
									}
								}

							}
						}
					}}
				}
			} else {
				icommandsender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Only players can use this command."));
			}

		}

	}

	@Override
	public boolean canCommandSenderUse(ICommandSender icommandsender) {
		return true;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public List getAliases() {
		// TODO Auto-generated method stub
		return this.aliases;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		// TODO Auto-generated method stub
		return null;
	}
}
