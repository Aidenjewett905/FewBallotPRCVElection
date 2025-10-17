import java.util.Scanner;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;

public class PerformElection {

	public static void main(String[] args) throws IOException {
		Scanner keyboard = new Scanner(System.in);
		File settingsFile = new File("settings.txt"); //settings.txt is the default file path
		Scanner settingsInput;
		
		ArrayList<Candidate> candidateList = new ArrayList<Candidate>();
		ArrayList<Ballot> ballotList = new ArrayList<Ballot>();

		while(!settingsFile.exists())
		{
			System.out.println("Default settings file not found");
			System.out.print("Enter the path for the settings file: ");
			settingsFile = new File(keyboard.nextLine());
		}
		settingsInput = new Scanner(settingsFile);
		
		//Get input from settings file via the default layout, any exceptions to be handled by default handler
		//settingsInput.next() twice will clear the "setting_name =" part of the line
		settingsInput.next();
		settingsInput.next();
		int numOfSeats = settingsInput.nextInt();
		settingsInput.nextLine();
		
		settingsInput.next();
		settingsInput.next();
		Scanner candidatesInput = new Scanner(new File(settingsInput.nextLine().trim()));
		
		settingsInput.next();
		settingsInput.next();
		Scanner ballotsInput = new Scanner(new File(settingsInput.nextLine().trim()));
		
		settingsInput.next();
		settingsInput.next();
		PrintWriter winnersOutput = new PrintWriter(new File(settingsInput.nextLine().trim()));
		
		settingsInput.close();
		
		//Get list of candidates
		candidatesInput.nextLine(); //Clear instruction line
		String[] candidateNameArray = candidatesInput.nextLine().split(", ");
		
		for(String candidateName : candidateNameArray)
		{
			candidateList.add(new Candidate(candidateName));
		}
		
		candidatesInput.close();
		
		if(candidateList.size() <= numOfSeats)
		{
			Queue<Candidate> winners = new LinkedList<Candidate>();
			while(candidateList.size() > 0)
			{
				winners.add(candidateList.remove(0));
			}
			while(winners.size() < numOfSeats)
			{
				winners.add(new Candidate("Vacant")); //All remaining seats cannot be filled by a candidate
			}
			
			Election.outputWinnersStatic(winnersOutput, winners);
			System.exit(0); //Election done
		}
		
		//Get ballots and set candidates
		ballotsInput.nextLine(); //Clear instructions
		while(ballotsInput.hasNext())
		{
			String[] ballotArray = ballotsInput.nextLine().split(", ");
			Candidate[] candidateArray = new Candidate[ballotArray.length];
			int candidateArrayIndex = 0;
			
			boolean candidateAdded = false; //Used to catch an invalid candidate on the ballot
			for(String candidateName : ballotArray)
			{
				//System.out.println("DEBUG Candidate checking: " + candidateName);
				for(int i = 0; i < candidateList.size(); i++)
				{
					if(candidateName.equals(candidateList.get(i).getName()) && !candidateAdded) //!candidateAdded is used to prevent out of bounds due to duplicate candidates
					{
						candidateArray[candidateArrayIndex] = candidateList.get(i);
						//System.out.println("DEBUG Candidate added: " + candidateName);
						candidateArrayIndex++;
						candidateAdded = true;
					}
				}
				if(!candidateAdded)
					System.out.printf("Candidate %s does not exist, they were not added to the ballot", candidateName);
				else
					candidateAdded = false;
			}
			
			ballotList.add(new Ballot(candidateArray));
		}
		
//		System.out.println("DEBUG: Num of ballots = " + ballotList.size());
		
		ballotsInput.close();
		
		Election election = new Election(ballotList, candidateList, numOfSeats);
		double thresholdPercentage = (100.0 / (numOfSeats + 1))/100;
		double voteThresholdDouble = ((thresholdPercentage * ballotList.size()) + 1); //Gets the threshold and cuts off the decimals, it is not supposed to be rounded
		int voteThreshold = (int)voteThresholdDouble;
		
//		System.out.println("DEBUG: Threshold Percentage = " + thresholdPercentage);
//		System.out.println("DEBUG: Vote Threshold before + 1 = " + (thresholdPercentage * ballotList.size()));
//		System.out.println("DEBUG: Vote Threshold Double = " + voteThresholdDouble);
//		System.out.println("DEBUG: Vote Threshold = " + voteThreshold);
		
		boolean allSeatsWon = false;
		boolean needsRunoffElection = false;
		while(!allSeatsWon) //Keep going until all seats are won
		{
			if(!election.processWinners(voteThreshold)) //This if statement calls processWinners so the processing is not supposed to happen outside of it
			{
				//If nobody won a seat this round, there must be a candidate removed
				if(!election.removeLastPlaceCandidates(numOfSeats))
				{
					//If removeLastPlaceCandidate returns false, there is an unbreakable tie
					System.out.println("Unbreakable Tie, a runoff election must be held");
					allSeatsWon = true;
					needsRunoffElection = true;
				}
			}
			if(election.getNumWinners() == numOfSeats)
				allSeatsWon = true;
		}
		
		if(needsRunoffElection)
			winnersOutput.println("There is an unbreakable tie, a runoff must be called to break it.\n");
		
		election.outputWinners(winnersOutput, voteThreshold);
		winnersOutput.close();
		keyboard.close();
		
	}

}
