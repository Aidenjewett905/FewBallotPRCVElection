import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

public class Election {
	
	private ArrayList<Ballot> ballotList; //List of all cast ballots
	private ArrayList<Candidate> candidateList; //List of all candidates
	private Candidate[] electionWinners; //Array that contains the winning candidates
	
	public Election(ArrayList<Ballot> ballotList, ArrayList<Candidate> candidateList, int numOfWinners) {
		this.ballotList = new ArrayList<>(ballotList);
		this.candidateList = new ArrayList<>(candidateList);
		electionWinners = new Candidate[numOfWinners];
	}
	
	public void setBallot(int index, Ballot ballot) {
		ballotList.set(index, ballot);
	}
	public void setCandidate(int index, Candidate candidate) {
		candidateList.set(index, candidate);
	}
	public void setWinner(int index, Candidate candidate) {
		electionWinners[index] = candidate;
	}
	
	public Ballot getBallot(int index) {
		return ballotList.get(index);
	}
	public Candidate getCandidate(int index) {
		return candidateList.get(index);
	}
	public Candidate getWinner(int index) {
		return electionWinners[index];
	}
	
}
