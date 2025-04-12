import java.util.ArrayList;

public class Ballot {
	
	public static int numOfBallots = 0;
	private ArrayList<String> rankedVotes; //Ordered list of the ranked candidates from 1st to last
	private int choice = 0; //Which of the ranked choices is this ballot currently on, this changes if the candidate before this choice were eliminated
	private boolean isEliminated;
	private boolean isAssigned;
	
	/**
	 * This constructor takes an arraylist of type String as a parameter and creates a ballot with those candidates on it.
	 * This arraylist should have the candidate names in ranked order from first choice to last choice.
	 * @param inputVotes an ArrayList of type String with candidates listed in ranked order first to last
	 */
	public Ballot(ArrayList<String> inputVotes) {
		for(String candidate : inputVotes)
		{
			rankedVotes.add(candidate);
		}
		numOfBallots++;
		isEliminated = false;
		isAssigned = false;
	}
	
	/**
	 * This constructor takes a list of type String as a parameter and creates a ballot with those candidates on it.
	 * This list should have the candidate names in ranked order from first choice to last choice.
	 * Any null values in the list will not be added, and will be skipped.
	 * @param inputVotes an ArrayList of type String with candidates listed in ranked order first to last
	 */
	public Ballot(String[] inputVotes) {
		for(String candidate : inputVotes)
		{
			if(candidate != null)
				rankedVotes.add(candidate);
		}
		numOfBallots++;
		isEliminated = false;
	}
	
	/**
	 * Gets which of the ranked choices the ballot currently counts towards
	 * @return the index of the choice, going from 1 to number of choices
	 */
	public int getCurrentChoice() {
		return choice;
	}
	
	/**
	 * This method checks if the ballot has been eliminated
	 * @return if the ballot has been eliminated
	 */
	public boolean isEliminated() {
		return isEliminated;
	}
	
	/**
	 * This method checks if the ballot has been assigned to a candidate
	 * @return if the ballot has been assigned to a candidate
	 */
	public boolean isAssigned() {
		return isAssigned;
	}
	
	/**
	 * Eliminates this ballot
	 */
	public void eliminate() {
		isEliminated = true;
		setAssignment(false);
		choice = -1;
	}
	
	/**
	 * Sets the assignment value of this ballot
	 * @param isAssigned is this ballot currently assigned to a candidates current votes
	 */
	public void setAssignment(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}
	
	/**
	 * This method will advance the ballots selected candidate to the next ranked candidate.
	 * If there are no more candidates, choice will be set to -1 and ballot will be marked as eliminated
	 */
	public void advanceChoice() {
		choice++;
		if(choice >= rankedVotes.size())
		{
			choice = -1;
			eliminate(); //All candidates this person voted for have been eliminated, so ballot is eliminated.
		}
		else
		{
			setAssignment(false);
		}
	}
	
	/**
	 * This method will return the name of the candidate that is currently chosen.
	 * @return The name of the candidate this ballot counts towards
	 */
	public String getCurrentCandidate() {
		return rankedVotes.get(getCurrentChoice());
	}
	
	/**
	 * This method gets the ranked candidate at the specified index (Directly from the candidate list).
	 * If the index is greater than or equal to the list size, it will return an error message and null.
	 * @param index the index of the candidate
	 * @return the candidate at the given index
	 */
	public String getCandidate(int index) {
		if(index < getBallotSize())
		{
			return rankedVotes.get(index);
		}
		else
		{
			System.out.printf("Error, index %d is invalid for this ballot", index);
			return null;
		}
	}
	
	/**
	 * This method returns the size of the ballot, i.e. the number of candidates on it.
	 * @return the size of the ballot
	 */
	public int getBallotSize() {
		return rankedVotes.size();
	}
	
}
