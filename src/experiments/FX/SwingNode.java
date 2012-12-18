package experiments.FX;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Swing "integration" on top of this JavaFX node
 *
 * @author arnaud nouard
 */
public class SwingNode extends Region {

    {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("mac") != -1) {
            STAGE_BORDER_X = 4;
            STAGE_BORDER_Y = 20;
        }
    }
    /*
     * Windows offet (Frame decoration bounds)
     * Should be dynamically determined
     */
    static public int STAGE_BORDER_X = 8;
    static public int STAGE_BORDER_Y = 30;
    JDialog jDialog;
    Stage stage;
    ChangeListener<Number> changeListenerH;
    ChangeListener<Bounds> changeListenerBIL;
    ChangeListener<Number> changeListenerW;
    ChangeListener<Number> changeListenerStageX;
    ChangeListener<Number> changeListenerStageY;
    JFrame jFrameParent;

    public SwingNode(Stage orgStage, final Component jcomponent) {
        this(orgStage, jcomponent, STAGE_BORDER_X, STAGE_BORDER_X);
    }

    public SwingNode(Stage orgStage, final Component jcomponent, int offsetX, int offsetY) {
        this.stage = orgStage;
        try {
            /*
             * Wrap the Swing component with an invisible window
             */
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    toDialog(jcomponent);
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(SwingNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
         * Stage's height
         */
        changeListenerH = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {

                Rectangle bounds = jDialog.getBounds();
                bounds.height = t1.intValue();
                jDialog.setBounds(bounds);
            }
        };
        heightProperty().addListener(changeListenerH);
        /*
         * Stage's Width
         */
        changeListenerW = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {

                Rectangle bounds = jDialog.getBounds();
                bounds.width = t1.intValue();
                jDialog.setBounds(bounds);
            }
        };
        widthProperty().addListener(changeListenerW);
        /*
         * Bounds in local
         */
        changeListenerBIL = new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds t, Bounds bounds) {

                final Bounds boundsJFX = localToScene(bounds);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Rectangle bounds = jDialog.getBounds();
                        bounds.x = (int) (boundsJFX.getMinX() + stage.getX()) + STAGE_BORDER_X;
                        bounds.y = (int) (boundsJFX.getMinY() + stage.getY()) + STAGE_BORDER_Y;
                        bounds.width = (int) boundsJFX.getWidth();
                        bounds.height = (int) boundsJFX.getHeight();

                        jDialog.setBounds(bounds);
                    }
                });
            }
        };
        boundsInLocalProperty().addListener(changeListenerBIL);
        /*
         * Stage X
         */
        changeListenerStageX = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, final Number t1) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Rectangle bounds = jDialog.getBounds();
                        bounds.x = t1.intValue();
                        Point2D jCoord = new Point2D(bounds.x, bounds.x);
                        Point2D p = localToScene(jCoord);

                        bounds.x = (int) p.getX() + STAGE_BORDER_X;
                        jDialog.setBounds(bounds);
                    }
                });
            }
        };
        stage.xProperty().addListener(changeListenerStageX);
        /*
         * Stage Y
         */
        changeListenerStageY = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, final Number t1) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Rectangle bounds = jDialog.getBounds();
                        bounds.y = t1.intValue();
                        Point2D jCoord = new Point2D(bounds.y, bounds.y);
                        Point2D p = localToScene(jCoord);

                        bounds.y = (int) p.getY() + STAGE_BORDER_Y;
                        jDialog.setBounds(bounds);
                    }
                });
            }
        };
        stage.yProperty().addListener(changeListenerStageY);
        /*
         * Stage's visibility
         */
        stage.showingProperty()
                .addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, final Boolean t1) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jDialog.setVisible(t1.booleanValue());
                    }
                });
            }
        });
        /*
         * Stage has focus
         */
        stage.focusedProperty()
                .addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, final Boolean newValue) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setSwingComponentAlwaysOnTop(newValue.booleanValue());
                    }
                });
            }
        });
    }

    public void removeAllListeners() {
        boundsInLocalProperty().removeListener(changeListenerBIL);
        heightProperty().removeListener(changeListenerH);
        widthProperty().removeListener(changeListenerW);
        stage.xProperty().removeListener(changeListenerStageX);
        stage.yProperty().removeListener(changeListenerStageY);
    }

    public Container toDialog(Component comp) {
        jFrameParent = new JFrame();
        jDialog = new JDialog(jFrameParent);
        jDialog.setVisible(false);
        setSwingComponentAlwaysOnTop(false);

        jDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Avoid ALT+F4

        jDialog.setUndecorated(true);
        jDialog.getContentPane().add(comp);

        jDialog.setType(Window.Type.UTILITY);
        jDialog.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        jDialog.setResizable(true);
        jDialog.setFocusable(true);

        jDialog.setAutoRequestFocus(false);

        jDialog.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setStageToFront();
                    }
                });
                setSwingComponentAlwaysOnTop(true);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                setSwingComponentAlwaysOnTop(false);
            }
        });

        // Focus
        jDialog.getContentPane().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                setSwingComponentAlwaysOnTop(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setStageToFront();
                    }
                });
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        // Window Focus
        jDialog.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                setSwingComponentAlwaysOnTop(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setStageToFront();
                    }
                });
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                jDialog.toBack();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.toBack();
                    }
                });
            }
        });
        return jDialog;
    }

    void setSwingComponentAlwaysOnTop(boolean value) {
        jDialog.setAlwaysOnTop(value);
    }

    void setStageToFront() {
        stage.toFront();

    }

    public void dispose() {
        removeAllListeners();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jFrameParent.dispose();
                jDialog.setAlwaysOnTop(false);
                jDialog.setVisible(false);
                jDialog.dispose();
            }
        });
    }
}
