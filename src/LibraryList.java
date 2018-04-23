import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class LibraryList extends JPanel{
	
	@SuppressWarnings("rawtypes")
	private JList library;
	private ArrayList <String> files;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LibraryList(){
		
		//Initialized the library
		files = new ArrayList<String>();
		library = new JList(files.toArray());
		JScrollPane libraryScrollPane = new JScrollPane(library);
		
		//sets up the default size for the library portion of the program
		libraryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		libraryScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		library.setBorder(BorderFactory.createTitledBorder("Library"));
		library.setVisibleRowCount(30);
		library.setFixedCellWidth(400);
		library.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//adds to the program
		this.add(libraryScrollPane);
	}
	
	//gives user access to the library directly	
	@SuppressWarnings("rawtypes")
	public JList getLibrary(){
		return library;
	}
	
	//adds the new file to the library
	public void addToLibrary(String newItem){
		files.add(newItem);
		filterLibrary();
	}
	
	//filters the library and removes any duplicates
	@SuppressWarnings("unchecked")
	public void filterLibrary(){
		for(int i=1; i<files.size(); i++){
			String album1 = files.get(i);
			String album2 = files.get(i-1);
			if(album1.equals(album2)){
				files.remove(album1);
			}
		}
		library.setListData(files.toArray());
	}

}
