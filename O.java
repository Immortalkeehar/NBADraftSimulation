import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
public class O {
	public static void main (String args[] ) throws IOException {		
		Scanner fileReader = new Scanner(new File("text.txt"));
		ArrayList<String> Teams = new ArrayList();
		ArrayList WinningPercentages = new ArrayList();
		int numOfBalls;
		numOfBalls = fileReader.nextInt();
		String readValue;
		while (fileReader.hasNext()) {
		readValue = fileReader.next();
		Teams.add(readValue);
		
		readValue = fileReader.next();
		WinningPercentages.add(readValue);
		}
		fileReader.close();
		ArrayList tickets = new ArrayList();
		getTickets(tickets, Teams, numOfBalls);
		int ticketPrint = 0;
		while (ticketPrint < tickets.size()) {
			System.out.println(tickets.get(ticketPrint));
			ticketPrint++;
		}
		System.out.println(tickets.size());
	}
	public static long factorial(long num) {
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
	public static long combinations(long group, long choose) {
		long result = factorial(group) / (factorial(choose) * factorial(group-choose));
		return result;
	}
	public static ArrayList getTickets(ArrayList tickets, ArrayList teams, int numOfBalls) {
		boolean isDuplicate = false;
		long combinations = combinations((long)teams.size(), (long)numOfBalls);
		while (tickets.size() < combinations) {
			ArrayList ballNums = new ArrayList();
			for (int addBalls = 1; addBalls < teams.size(); addBalls++) {
				ballNums.add(addBalls);
			}
			int randomNums = 0;
			int drawnBallCounter = 0;
			while (drawnBallCounter < numOfBalls) { 
				int drawnBallIndex = (int)(Math.random()*ballNums.size());
				randomNums = randomNums*10 + (int)ballNums.get(drawnBallIndex);
				drawnBallCounter++;
				ballNums.remove(Integer.valueOf(drawnBallIndex));
			}
			for (int checkForDuplicate = 0; checkForDuplicate < tickets.size(); checkForDuplicate++) {
				if ((int)tickets.get(checkForDuplicate) == randomNums) {
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
}


