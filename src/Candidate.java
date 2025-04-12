import java.util.ArrayList;

public class Candidate implements Comparable<Candidate> {

	private ArrayList<Ballot> currentVotes = new ArrayList<Ballot>(); //Ballots with this candidate as their current choice
	private int totalVotes; //Total votes across all ballots for this candidate
	private String name; //Name of candidate
	
	/**
	 * Constructor sets candidate name, but does not add any ballots. Sets the default total votes to 0.
	 * @param name the name of the candidate
	 */
	public Candidate(String name) {
		setName(name);
		setTotalVotes(0);
	}
	/**
	 * Constructor sets the candidates name, and adds the provided ballots to the candidate if they list the candidate as their choice.
	 * @param name the name of the candidate
	 * @param votes a list of ballots that will be added to this candidate if their current choice is this candidate
	 */
	public Candidate(String name, ArrayList<Ballot> votes) {
		addBallotList(votes);
		setName(name);
		setTotalVotes(getVotes());
	}
	
	/**
	 * This method sets the candidates name
	 * @param name the name of the candidate
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * This method adds the given ballot to the candidate, so long as the ballots choice is this candidate
	 * @param vote the ballot being added
	 */
	public void addBallot(Ballot vote) {
		if(vote.getCurrentCandidate().equals(getName()))
		{
			currentVotes.add(vote); //Ballot matches candidate
		}
	}
	/**
	 * This method adds all valid ballots to the candidate.
	 * A ballot is valid if it's current choice is the candidate it is being added to.
	 * @param votes a list of ballots
	 */
	public void addBallotList(ArrayList<Ballot> votes) {
		for(Ballot vote : votes)
		{
			addBallot(vote);
		}
	}
	/**
	 * This method sets the total number of votes this candidate has received.
	 * The total votes is used in tiebreakers and is the number of ballots that this candidate shows up on at any position.
	 * @param totalVotes the total votes of this candidate
	 */
	public void setTotalVotes(int totalVotes) {
		this.totalVotes = totalVotes;
	}
	/**
	 * This method will declare the current candidate as having won a seat.
	 * The threshold for winning will determine how many ballots will be eliminated, with the ballots after the threshold going to their next choice.
	 * @param threshold the threshold number of votes to win a seat
	 */
	public void wonSeat(int threshold) {
		if(getVotes() >= threshold)
		{
			for(int i = 0; i < threshold; i++)
			{
				currentVotes.get(i).eliminate(); //Eliminates ballots that counted towards winning
			}
			while(getVotes() > threshold)
			{
				currentVotes.remove(threshold).advanceChoice();
			}
		}
	}
	
	/**
	 * This method eliminates the current candidate.
	 * This is done by removing all the current votes for this candidate, and advancing the ballots to their next choice.
	 */
	public void eliminate() {
		while(getVotes() > 0)
		{
			currentVotes.removeLast().advanceChoice();
		}
	}
	
	/**
	 * This method gets the name of the candidate.
	 * @return the name of the candidate
	 */
	public String getName() {
		return name;
	}
	/**
	 * This method returns the number of votes this candidate has received.
	 * @return the number of votes this candidate has received
	 */
	public int getVotes() {
		return currentVotes.size();
	}
	/**
	 * This method gets the total number of votes for this candidate across all ballots.
	 * @return the number of ballots that this candidate shows up on
	 */
	public int getTotalVotes() {
		return totalVotes;
	}
	
	public String toString() {
		return String.format("Candidate %s with %d votes and %d total votes", getName(), getVotes(), getTotalVotes());
	}
	
	public int compareTo(Candidate candidate2) {
		if(getVotes() == candidate2.getVotes()) //If same number of votes
		{
			return getTotalVotes() - candidate2.getTotalVotes(); //Return the difference between total votes
		}
		else //If different number of votes
		{
			return getVotes() - candidate2.getVotes(); //Return the difference between votes
		}
	}
	
}
