import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class Controller extends JPanel{
	private JToolBar toolbar;
	private JButton rewind;
	private JButton play;
	private JButton stop;
	private JButton pause;
	private JButton fastforward;
	private JLabel currentlyPlaying;
	
	public Controller(){
		//loads up the toolbar
		toolbar = new JToolBar();
		
		//brings images for the buttons
		Image rewindImage = null;
		try {
			rewindImage = Toolkit.getDefaultToolkit().getImage("pictures/rewind.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Image playImage = null;
		try {
			playImage = Toolkit.getDefaultToolkit().getImage("pictures/play.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Image stopImage = null;
		try {
			stopImage = Toolkit.getDefaultToolkit().getImage("pictures/stop.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Image pauseImage = null;
		try {
			pauseImage = Toolkit.getDefaultToolkit().getImage("pictures/pause.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Image fastforwardImage = null;
		try {
			fastforwardImage = Toolkit.getDefaultToolkit().getImage("pictures/fastforward.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//places images onto icons
		ImageIcon rewindIcon = new ImageIcon(rewindImage);
		ImageIcon playIcon = new ImageIcon(playImage);
		ImageIcon stopIcon = new ImageIcon(stopImage);
		ImageIcon pauseIcon = new ImageIcon(pauseImage);
		ImageIcon fastforwardIcon = new ImageIcon(fastforwardImage);
		
		//assigns the icons to the buttons
		rewind = new JButton(rewindIcon);
		play = new JButton(playIcon);
		stop = new JButton(stopIcon);
		pause = new JButton(pauseIcon);
		fastforward = new JButton(fastforwardIcon);
		currentlyPlaying = new JLabel();
		
		//assigns the the buttons to the toolbar
		toolbar.add(stop);
		toolbar.addSeparator();
		toolbar.add(rewind);
		toolbar.add(play);
		toolbar.add(pause);
		pause.setVisible(false);
		toolbar.add(fastforward);
		toolbar.addSeparator();
		toolbar.add(currentlyPlaying);
		
		//sets the toolbar to be non floatable
		toolbar.setFloatable(false);
		
		//adds the toolbar
		this.add(toolbar);
	}
	
	//toggles the visibility so only play or pause is visible at one time
	public void togglePlayPause(){
		if(pause.isVisible() == true){
			pause.setVisible(false);
			play.setVisible(true);
		}
		else{
			pause.setVisible(true);
			play.setVisible(false);
		}
	}

	public JToolBar getToolbar() {
		return toolbar;
	}

	public JButton getRewind() {
		return rewind;
	}

	public JButton getPlay() {
		return play;
	}

	public JButton getStop() {
		return stop;
	}

	public JButton getPause() {
		return pause;
	}

	public JButton getFastforward() {
		return fastforward;
	}
	
	//sets the currently playing to the currently playing song
	public void setCurrentlyPlaying(String n){
		currentlyPlaying.setText(n);
	}

}
