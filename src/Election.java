import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.io.PrintWriter;

public class Election {
	
	private ArrayList<Ballot> ballotList; //List of all cast ballots
	private ArrayList<Candidate> candidateList; //List of all candidates
	private Queue<Candidate> electionWinners; //Array that contains the winning candidates
	
	/**
	 * Constructor for the Election class, which is designed to perform the important operations 
	 * that are needed for an election
	 * @param ballotList List of ballots cast
	 * @param candidateList List of candidates running
	 * @param numOfWinners How many seats are there
	 */
	public Election(ArrayList<Ballot> ballotList, ArrayList<Candidate> candidateList, int numOfWinners) {
		this.ballotList = new ArrayList<>(ballotList);
		this.candidateList = new ArrayList<>(candidateList);
		Collections.sort(candidateList); //Sorts the candidate list in order of votes
		electionWinners = new LinkedList<Candidate>();
	}
	
	/**
	 * Sets the ballot at a given index to another ballot object
	 * @param index The index to set the new ballot
	 * @param ballot The new ballot object
	 */
	public void setBallot(int index, Ballot ballot) {
		ballotList.set(index, ballot);
	}
	/**
	 * Adds a ballot to the end of the ballot list
	 * @param ballot The ballot being added
	 */
	public void addBallot(Ballot ballot) {
		ballotList.add(ballot);
	}
	/**
	 * Removes a ballot from the list at the first index where it occurs.
	 * @param ballot The ballot to be removed
	 * @return True if a ballot was removed, false if no ballot was removed
	 */
	public boolean removeBallot(Ballot ballot) {
		return ballotList.remove(ballot);
	}
	/**
	 * Sets an index in the list of candidates to a candidate object
	 * @param index The index to be set
	 * @param candidate The new candidate object
	 */
	public void setCandidate(int index, Candidate candidate) {
		candidateList.set(index, candidate);
		Collections.sort(candidateList); //Sorts the candidate list in order of votes
	}
	/**
	 * Add a candidate to the end of the list
	 * @param candidate The candidate to be added
	 */
	public void addCandidate(Candidate candidate) {
		candidateList.add(candidate);
		Collections.sort(candidateList); //Sorts the candidate list in order of votes
	}
	/**
	 * Remove a candidate at the first index where it occurs
	 * @param candidate The candidate to be removed
	 * @return If the candidate was found and removed or not
	 */
	public boolean removeCandidate(Candidate candidate) {
		boolean wasRemoved = candidateList.remove(candidate);
		Collections.sort(candidateList); //Sorts the candidate list in order of votes
		return wasRemoved;
	}
	/**
	 * Adds a candidate to the list of candidates that have won
	 * @param candidate The candidate that has won
	 */
	public void addWinner(Candidate candidate) {
		electionWinners.add(candidate);
	}
	
	/**
	 * Gets a ballot from the list of ballots at the given index. If the index is out of bounds, null is returned.
	 * @param index The index of the ballot to return
	 * @return The ballot at the provided index, or null if the index is out of bounds
	 */
	public Ballot getBallot(int index) {
		if(index >= ballotList.size() || index < 0) 
			return null;
		else
			return ballotList.get(index);
	}
	/**
	 * Returns the candidate at the given index. If the index is out of bounds, null is returned
	 * @param index Index of the candidate to return
	 * @return The candidate at the provided index, or null if the index is out of bounds
	 */
	public Candidate getCandidate(int index) {
		if(index >= candidateList.size() || index < 0) 
			return null;
		else
			return candidateList.get(index);
	}
	/**
	 * Removes a winner from the list of winners in the order that they won (1st, 2nd, 3rd, etc)
	 * @return The winner that has been in this list the longest
	 */
	public Candidate removeWinner() {
		return electionWinners.poll();
	}
	/**
	 * Gets number of candidates that have won so far
	 * @return The number of candidates that have won so far
	 */
	public int getNumWinners() {
		return electionWinners.size();
	}
	
	/**
	 * This method will check to see if any candidates have reached the vote threshold and won. True will be returned if a seat has been won.
	 * @param threshold The minimum number of votes required to win a seat
	 * @return If a candidate has won a seat or not
	 */
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
	
	/**
	 * This method will remove last place candidates to the best of its ability.
	 * The specific process is: First check if the last place candidate is tied with any other candidate, this will be decided either through number of votes or total votes if that does not work.
	 * Second: If every tied candidate can be eliminated without leaving seats vacant, eliminate them
	 * Third: If every tied candidate can win without going over the number of available seats, they all win
	 * Fourth: If all candidates cannot win or be eliminated, the seats are tied and a runoff must be held
	 * @param numTotalSeats The number of seats that can be won in total (not remaining number of seats)
	 * @return If candidates were removed or if all have won. If false is returned, there was an unbreakable tie.
	 */
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
	
	/**
	 * Outputs a list of the winning candidates, in order of winning, to the file provided in the PrintWriter
	 * @param output A PrintWriter object for the output file for the winning candidates
	 */
	public void outputWinners(PrintWriter output) {
		for(int i = 1; getNumWinners() != 0; i++)
		{
			String numPostfix = "th";
			if(i == 1)
				numPostfix = "st";
			else if(i == 2)
				numPostfix = "nd";
			else if(i == 3)
				numPostfix = "rd";
			
			output.printf("%d%s seat: %s\n", i, numPostfix, removeWinner());
		}
	}
	
	/**
	 * Outputs a list of the winning candidates, in order of winning, to the file provided in the PrintWriter.
	 * This is a static variant of the method that takes a queue of winning candidates as an input
	 * @param output A PrintWriter object for the output file for the winning candidates
	 */
	public static void outputWinnersStatic(PrintWriter output, Queue<Candidate> winners) {
		for(int i = 1; winners.size() != 0; i++)
		{
			String numPostfix = "th";
			if(i == 1)
				numPostfix = "st";
			else if(i == 2)
				numPostfix = "nd";
			else if(i == 3)
				numPostfix = "rd";
			
			output.printf("%d%s seat: %s\n", i, numPostfix, winners.poll());
		}
	}
	
}
