import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar{
	
	private JMenu file;
	private JMenu library;
	private JMenu help;

	private JMenuItem exit;
	private JMenuItem libraryLocation;
	private JMenuItem about;
	private JMenuItem reloadLibrary;
	private JMenuItem addFilesToLibrary;
	
	public MenuBar()
	{
		//items for the menu bar with mnemonics
		file = new JMenu("File");
		file.setMnemonic('F');
		library = new JMenu("Library");
		library.setMnemonic('L');
		help = new JMenu("Help");
		help.setMnemonic('H');
		
		//sub items for the menu bar
		exit = new JMenuItem("Exit");
		libraryLocation = new JMenuItem("Library Location");
		about = new JMenuItem("About");
		reloadLibrary = new JMenuItem("Reload Library");
		addFilesToLibrary = new JMenuItem("Add Songs to Library");
		
		//adds sub items to the main menu bar items
		file.add(exit);
		library.add(libraryLocation);
		library.add(addFilesToLibrary);
		library.add(reloadLibrary);
		help.add(about);
		
		//adds main items to the menu bar
		this.add(file);
		this.add(library);
		this.add(help);
	}

	public JMenu getFile() {
		return file;
	}

	public JMenu getLibrary() {
		return library;
	}

	public JMenu getHelp() {
		return help;
	}

	public JMenuItem getExit() {
		return exit;
	}

	public JMenuItem getLibraryLocation() {
		return libraryLocation;
	}

	public JMenuItem getAbout() {
		return about;
	}
	
	public JMenuItem getReloadLibrary(){
		return reloadLibrary;
	}
	
	public JMenuItem getAddFilesToLibrary(){
		return addFilesToLibrary;
	}
	
}
