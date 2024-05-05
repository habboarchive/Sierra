package sierra.boot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import sierra.Sierra;



public class CommandLine implements Runnable {

	private static CommandLine commandListener = new CommandLine();

	public static void start() {
		Thread commandThread = new Thread(commandListener);
		commandThread.start();
	}

	@Override
	public void run() {

		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {

			try
			{
				String input = consoleReader.readLine();

				if (input == null)
					continue;
				else
					handleCommand(input);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}

	public void handleCommand(String command)
	{
		if (command.startsWith("help"))
		{
			System.out.println("Avaliable commands are: ha, help");
		}
		
		if (command.startsWith("ha"))
		{
			String message = command.replace("ha ", "");
			
			Sierra.getSocketFactory().getSessionManager().sendNotify(message);
		}
	}
}
