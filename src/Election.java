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
	
	//public boolean removeLastPlaceCandidates() {
	//	
	//}
	
}
