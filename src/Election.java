import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

public class Election {
	
	private ArrayList<Ballot> ballotList; //List of all cast ballots
	private ArrayList<Candidate> candidateList; //List of all candidates
	private Queue<Candidate> electionWinners; //Array that contains the winning candidates
	
	public Election(ArrayList<Ballot> ballotList, ArrayList<Candidate> candidateList, int numOfWinners) {
		this.ballotList = new ArrayList<>(ballotList);
		this.candidateList = new ArrayList<>(candidateList);
		Collections.sort(candidateList); //Sorts the candidate list in order of votes
		electionWinners = new LinkedList<Candidate>();
	}
	
	public void setBallot(int index, Ballot ballot) {
		ballotList.set(index, ballot);
	}
	public void addBallot(Ballot ballot) {
		ballotList.add(ballot);
	}
	public void removeBallot(Ballot ballot) {
		ballotList.remove(ballot);
	}
	public void setCandidate(int index, Candidate candidate) {
		candidateList.set(index, candidate);
		Collections.sort(candidateList); //Sorts the candidate list in order of votes
	}
	public void addCandidate(Candidate candidate) {
		candidateList.add(candidate);
		Collections.sort(candidateList); //Sorts the candidate list in order of votes
	}
	public void removeCandidate(Candidate candidate) {
		candidateList.remove(candidate);
		Collections.sort(candidateList); //Sorts the candidate list in order of votes
	}
	public void addWinner(Candidate candidate) {
		electionWinners.add(candidate);
	}
	
	public Ballot getBallot(int index) {
		if(index >= ballotList.size() || index < 0) 
			return null;
		else
			return ballotList.get(index);
	}
	public Candidate getCandidate(int index) {
		if(index >= candidateList.size() || index < 0) 
			return null;
		else
			return candidateList.get(index);
	}
	public Candidate removeWinner() {
		return electionWinners.poll();
	}
	public int getNumWinners() {
		return electionWinners.size();
	}
	
	public boolean processWinners(int threshold) { //Returns true if a seat has been won
		for(Candidate candidate : candidateList)
		{
			if(candidate.wonSeat(threshold)) //wonSeat will also redistribute the excess ballots to the next candidates on its own if the candidate won
			{
				addWinner(candidate);
				removeCandidate(candidate);
				return true; //A candidate won a seat
			}
		}
		return false; //No seats were won
	}
	
	public boolean removeLastPlaceCandidates(int numTotalSeats) { //Returns false if there is an unbreakable tie
		int numTied = 1; //If the candidate is tied with the next one, then that means there are 2 tied candidates overall, so by default there is 1 tied candidate.
		boolean noMoreTied = false;
		int availableSeats = numTotalSeats - getNumWinners();
		
		int index = 0;
		while(!noMoreTied)
		{
			if(candidateList.get(index).compareTo(candidateList.get(++index)) == 0) //This increments the index too
			{
				numTied++; //There is another tied candidate
			}
			else
				noMoreTied = true;
		}
		
		int numOfLosers = candidateList.size() - availableSeats; //Number of total candidates that have not won minus available seats
		if(numTied <= numOfLosers) //If all remaining candidates can lose, eliminate them
		{
			while(numTied > 0)
			{
				candidateList.remove(0).eliminate();
				numTied--;
			}
			
			Collections.sort(candidateList); //Sort the candidates now that votes have been redistributed from eliminated candidates
			
			return true; //Candidates were eliminated
		}
		else if(numOfLosers <= 0) //If no other candidates must lose, then all can win
		{
			while(numTied > 0)
			{
				electionWinners.add(candidateList.remove(0));
				numTied--;
			}
			while(getNumWinners() < numTotalSeats)
			{
				electionWinners.add(new Candidate("Vacant")); //All remaining seats cannot be filled by a candidate
			}
			
			return true; //All candidates won
		}
		else //If the tied candidates can't all lose or win, then there is a tie that must be broken with a runoff or other process
		{
			while(getNumWinners() < numTotalSeats) //For all remaining seats
			{
				electionWinners.add(new Candidate("Tied")); //All remaining seats are tied
			}
			
			return false; //Unbreakable tie, no candidates removed
		}
		
	}
	
}
