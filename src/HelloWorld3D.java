import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HelloWorld3D extends Frame {


    private Transform3D spin;
    private double angle = 0;
    private TransformGroup trans;

    private Canvas3D canv;


    public static void main(String[] args) {
        System.out.println("LD Library Path:" + System.getProperty("java.library.path"));
        new HelloWorld3D();

    }

    public HelloWorld3D() {
        setLayout(new BorderLayout());
        GraphicsConfiguration gc =
                SimpleUniverse.getPreferredConfiguration();

        canv = new Canvas3D(gc);
        canv.setSize(800, 800);
        add("Center", canv);
        SimpleUniverse univ = new SimpleUniverse(canv);
        univ.getViewingPlatform().setNominalViewingTransform();
        univ.addBranchGraph(createContentBranch());

        // Обработка событий
        canv.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                //	super.keyPressed(ke);
                if (ke.getKeyChar() == 'a') { angle += 0.01;
                    spin.setRotation(new AxisAngle4d(1, 1, 0, angle));
                }
                System.out.println(angle); trans.setTransform(spin);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) { super.windowClosing(arg0);
                System.exit(0);
            }
        });

        pack();
        setVisible(true);

    }


    protected BranchGroup createContentBranch() {
        BranchGroup bg = new BranchGroup();


        // Add texture
        Appearance ap = new Appearance();
        TextureLoader tl = new TextureLoader("wood2.jpg",this); ImageComponent2D ic =tl.getImage();

        Texture2D tex =
                new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, ic.getWidth(),ic.getHeight());

        tex.setImage(0,ic);
        TextureAttributes ta = new TextureAttributes(); ta.setTextureMode(TextureAttributes.REPLACE);

        TexCoordGeneration tcg =
                new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
                        TexCoordGeneration.TEXTURE_COORDINATE_2);
        ap.setTexture(tex); ap.setTextureAttributes(ta);

        ap.setMaterial(new Material());
        ap.setTexCoordGeneration(tcg);
//        bg.addChild(new Box(1.0f, 1.0f, 1.0f, ap));



        trans = new TransformGroup();
        trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        bg.addChild(trans);
        trans.addChild(new ColorCube(0.4));
        spin = new Transform3D();
        spin.setRotation(new AxisAngle4d(1, 1, 0, angle));
        trans.setTransform(spin);

        Color3f ambient = new Color3f(0.5f, 0.5f, 0.5f); // окуржающий
        Color3f emissive = new Color3f(0.0f, 0.0f, 1.0f); // свечение
        Color3f diffuse = new Color3f(0.0f, 0.0f, 1.0f); // дифузионный
        Color3f specular = new Color3f(1.0f, 1.0f, 1.0f); // зеркальный
        float shininess = 1200.0f;
        Appearance shinyBlueApp = new Appearance();
        Material shinyBlueMat =
                new Material(ambient, emissive, diffuse, specular, shininess);
        shinyBlueApp.setMaterial(shinyBlueMat);

        // Добавляем цилиндр с указанными свойствами
        bg.addChild(new Cylinder(0.3f, 1.5f, shinyBlueApp));
        addLights(bg);
        


        bg.compile();
        return bg;

    }

    protected void addLights(BranchGroup bg) {
        BoundingSphere bounds =
                new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Color3f ambientColour = new Color3f(0.2f, 0.2f, 0.2f);
        AmbientLight ambLight =
                new AmbientLight(ambientColour);
        ambLight.setInfluencingBounds(bounds);


        Color3f dirColour = new Color3f(0.5f,0.5f,0.5f);
        Vector3f lightDir = new Vector3f(-1.0f,-1.0f,-1.0f);

        DirectionalLight dirLight=
                new DirectionalLight(dirColour,lightDir);
        dirLight.setInfluencingBounds(bounds); bg.addChild(dirLight);

        bg.addChild(ambLight);
    }
}