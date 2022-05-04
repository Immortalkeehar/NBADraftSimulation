import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
public class Mock_NBA_Draft { // currently works
	public static void main (String args[] ) throws IOException {	
		System.out.println("Welcome to the mock NBA 2022 Draft!");
		System.out.println();
		Scanner fileReader = new Scanner(new File("input.txt"));
		ArrayList<String> Teams = new ArrayList<String>();
		ArrayList<Double> WinningPercentages = new ArrayList<Double>();
		int numOfBalls;
		numOfBalls = fileReader.nextInt(); // input file reading
		fileReader.nextLine();
		String readValue;
		while (fileReader.hasNext()) {
		readValue = fileReader.nextLine();
		Teams.add(readValue); // reads team names
		
		readValue = fileReader.nextLine();
		WinningPercentages.add(Double.parseDouble(readValue)); // reads winning percentages
		}
		fileReader.close();
		ArrayList tickets = new ArrayList();
		getTickets(tickets, Teams, numOfBalls);
		ArrayList<ArrayList<String>> teamsTickets = new ArrayList<ArrayList<String>>();
		teamsTickets = assignTickets(Teams, WinningPercentages, tickets);
		teamsTickets.remove(teamsTickets.size()-1);
		printTeamAssignedTicketsToFile(teamsTickets, Teams, tickets, numOfBalls);
		findWinningTicketAndEndSequence(teamsTickets, Teams, numOfBalls);
		
	}
	public static long factorial(long num) { // used for combinations calculation
		long result = num;
		num--;
		if (num>0) {
			while (num>0) {
				result *= num;
				num--;
			}
			return result;
		}
		return 0;
	}
	public static long combinations(long group, long choose) { // used to calculate proper number of tickets needed in total
		long result = factorial(group) / (factorial(choose) * factorial(group-choose));
		return result;
	}
	public static ArrayList<String> getTickets(ArrayList<String> tickets, ArrayList<String> teams, int numOfBalls) {
		boolean isDuplicate = false;
		long combinations = combinations((long)teams.size(), (long)numOfBalls);
		while (tickets.size() < combinations) { // creates proper number of tickets thru random generation
			ArrayList<Integer> ballNums = new ArrayList<Integer>();
			for (int addBalls = 1; addBalls <= teams.size(); addBalls++) { //gives values from 1 - 14 in indexes 0 - 13
				ballNums.add(addBalls);
			}
			String randomNums = "";
			int drawnBallCounter = 0;
			while (drawnBallCounter < numOfBalls) { // drawn balls is the same number as numbers in a ticket
				int drawnBallIndex = (int)(Math.random()*ballNums.size());
				randomNums += ballNums.get(drawnBallIndex) + " ";
				drawnBallCounter++;
				ballNums.remove(ballNums.get(drawnBallIndex));
			}
			randomNums = sortFromLeastToGreatest(randomNums, numOfBalls).strip();
			for (int checkForDuplicate = 0; checkForDuplicate < tickets.size(); checkForDuplicate++) { // avoids duplicate tickets
				if (tickets.get(checkForDuplicate).equals(randomNums)) {
					isDuplicate = true;
					
				} 
			}
			if (!isDuplicate) {
				tickets.add(randomNums);
			}
			isDuplicate = false;
		}
		return tickets;
	}
	public static String sortFromLeastToGreatest(String ticket, int numOfBalls) {
		String[] strTicket = new String[numOfBalls];
		int[] intTicket = new int[numOfBalls];
		strTicket = ticket.split(" ");
		for (int intConverter = 0; intConverter < strTicket.length; intConverter++) { // make all nums into an array of ints
			intTicket[intConverter] = Integer.parseInt(strTicket[intConverter]);
		}
		for (int arrayLength = 0; arrayLength < intTicket.length; arrayLength++) { // sort list of ints from least to greatest
			for (int arraySorter = 0; arraySorter < intTicket.length-1; arraySorter++) {
				if (intTicket[arraySorter] > intTicket[arraySorter+1]) {
					int initialValue = intTicket[arraySorter+1];
					intTicket[arraySorter+1] = intTicket[arraySorter];
					intTicket[arraySorter] = initialValue;
				}
			}
		}
		String newTicket = "";
		for (int stringConverter = 0; stringConverter < intTicket.length; stringConverter++) { // convert back to a string
			newTicket += intTicket[stringConverter] + " ";
		}
		
		return newTicket;
	}
	public static ArrayList<ArrayList<String>> assignTickets(ArrayList<String> teams, ArrayList<Double> winningPercentages, ArrayList<String> tickets) {
		ArrayList<ArrayList<String>> teamsTickets = new ArrayList<ArrayList<String>>();
		for (int teamTracker = 0; teamTracker < teams.size(); teamTracker++) { // gives team names
			ArrayList<String> teamNamePlaceHolder = new ArrayList<String>();
			teamsTickets.add(teamNamePlaceHolder);
		}
		teamsTickets.add(teams);
		int ticketTracker = 1; // accounts for burnt ticket
		int ticketGoal = 1;
		for (int teamCounter = 0; teamCounter < teams.size(); teamCounter++) { // gives tickets to teams
			ticketGoal += Math.round((winningPercentages.get(teamCounter)/100) * (tickets.size()-1));
			while (ticketTracker < ticketGoal) {
				teamsTickets.get(teamCounter).add(tickets.get(ticketTracker));
				ticketTracker++;
			}
		}
		return teamsTickets;
	}
	public static void findAndPrintPossibleWinners(ArrayList<ArrayList<String>> teamsTickets, ArrayList<String> teams, String winningTicket, int drawnBallCounter) {
		ArrayList<String> possibleWinners = new ArrayList<String>();
		boolean dupe = false;
		winningTicket = sortFromLeastToGreatest(winningTicket, drawnBallCounter).trim();
		for (int teamTracker = 0; teamTracker < teamsTickets.size(); teamTracker++) { // goes thru teams and tickets
			for (int ticketTracker = 0; ticketTracker < teamsTickets.get(teamTracker).size(); ticketTracker++) {
			if (teamsTickets.get(teamTracker).get(ticketTracker).indexOf(winningTicket) > -1) {
				for (int dupeChecker = 0; dupeChecker < possibleWinners.size(); dupeChecker++) {
					if (possibleWinners.get(dupeChecker).equals(teams.get(teamTracker))) { // searches for duplicate winner
						dupe = true;
					}
				}
				if (!dupe) {
					String teamName = teams.get(teamTracker);
					possibleWinners.add(teamName);
				}
				dupe = false;
			}
			}
			
		}
		System.out.println("The possible winning teams could be: " + possibleWinners);
		
		
	}
	public static void findWinningTicketAndEndSequence(ArrayList<ArrayList<String>> teamsTickets, ArrayList<String> teams, int numOfBalls) {
		Scanner keyboard = new Scanner(System.in);
		ArrayList<String> winners = new ArrayList<String>();
		boolean isDuplicateWinner = false;
		for (int numOfRounds = 1; numOfRounds <= numOfBalls; numOfRounds++) { // runs this the [number of balls] amount of times
			System.out.println("Round #" + numOfRounds);
			System.out.println();
			ArrayList<Integer> ballNums = new ArrayList<Integer>();
			for (int addBalls = 1; addBalls <= teams.size(); addBalls++) { // assigns balls 1 - 14 into indexes 0 - 13
				ballNums.add(addBalls);
			}
			String winningTicket = "";
			int drawnBallCounter = 0;
			while (drawnBallCounter < numOfBalls) { 
				int numBall = drawnBallCounter+1;
				System.out.println("Press 'Enter' to draw ball #" + numBall);
				keyboard.nextLine(); // reads the 'Enter' input
				int drawnBallIndex = (int)(Math.random()*ballNums.size()); // randomizes the balls to draw
				System.out.println();
				System.out.println("Ball #" + numBall + " is: " + ballNums.get(drawnBallIndex)); // draws ball
				System.out.println();
				winningTicket += ballNums.get(drawnBallIndex) + " "; // adds the ball number to the winning ticket
				drawnBallCounter++;
				ballNums.remove(ballNums.get(drawnBallIndex)); // removes the ball number from list to draw from
				if (drawnBallCounter < numOfBalls) {
					findAndPrintPossibleWinners(teamsTickets, teams, winningTicket, drawnBallCounter); // get possible winners
				}
			}
			System.out.println("The winning combination is: " + winningTicket);
			String winningTeam = findWinningTeam(teamsTickets, teams, winningTicket, numOfBalls);
			for (int winnerTracker = 0; winnerTracker < winners.size(); winnerTracker++) { // finds duplicate winners
				if (winners.get(winnerTracker).equals(winningTeam)) {
					isDuplicateWinner = true;
				}
			}
			if (isDuplicateWinner) {
				System.out.println("The winning ticket was assigned to " + winningTeam + ", but they have already won so the round will restart"); // what happens when there is a duplicate winner
				numOfRounds--;
			}
			else {
				System.out.println("The winner of round " + numOfRounds + " is the " + winningTeam); // prints winner
				winners.add(winningTeam);
			}
			isDuplicateWinner = false;
			System.out.println();
		}
		System.out.println("Press 'Enter' to see the pick order");
		keyboard.nextLine(); // reads 'Enter' input
		printDraftOrder(winners, teams);
		keyboard.close();
	}
	public static String findWinningTeam(ArrayList<ArrayList<String>> teamsTickets, ArrayList<String> teams, String winningTicket, int numOfBalls) {
		int winningTeamIndex = 0;
		String sortedWinningTicket = sortFromLeastToGreatest(winningTicket, numOfBalls).strip();
		for (int teamIndex = 0; teamIndex < teamsTickets.size(); teamIndex++) { // goes thru all teams and tickets
			for (int ticketIndex = 0; ticketIndex < teamsTickets.get(teamIndex).size(); ticketIndex++) {
				if ((teamsTickets.get(teamIndex).get(ticketIndex)).equals(sortedWinningTicket)) { // finds team with winning ticket
					winningTeamIndex = teamIndex;
				}
			}
		}
		return teams.get(winningTeamIndex);
	}
	public static void printDraftOrder(ArrayList<String> winners, ArrayList<String> teams) {
		for (int winnerTracker = 0; winnerTracker < winners.size(); winnerTracker++) {
			teams.remove(winners.get(winnerTracker));
		}
		int pickNumber = 1;
		for (int winnerPrinter = 0; winnerPrinter < winners.size(); winnerPrinter++) {
			System.out.println("Pick #" + pickNumber + " goes to: " + winners.get(winnerPrinter)); // prints the winners first
			pickNumber++;
		}
		for (int loserPrinter = 0; loserPrinter < teams.size(); loserPrinter++) {
			System.out.println("Pick #" + pickNumber + " goes to: " + teams.get(loserPrinter)); // prints the other teams in an order reverse of their season record
			pickNumber++;
		}
	}
	public static void printTeamAssignedTicketsToFile(ArrayList<ArrayList<String>> teamsTickets, ArrayList<String> teams, ArrayList<String> tickets, int numOfBalls) throws IOException {
		PrintWriter toFile = new PrintWriter("TeamsAndAssignedTickets.txt");
		toFile.println("Burned ticket: " + sortFromLeastToGreatest(tickets.get(0), numOfBalls));
		toFile.println();
		for (int teamTracker = 0; teamTracker < teamsTickets.size(); teamTracker++) {
			toFile.println(teams.get(teamTracker) + ": "); // teams print
			for (int ticketTracker = 0; ticketTracker < teamsTickets.get(teamTracker).size(); ticketTracker++) {
				toFile.print(teamsTickets.get(teamTracker).get(ticketTracker) + ", "); // all tickets
			}
			toFile.println();
			toFile.println(teamsTickets.get(teamTracker).size() + " combinations have been assigned to this team"); // num of tickets assigned
			toFile.println();
		}
		toFile.close();
	}
}