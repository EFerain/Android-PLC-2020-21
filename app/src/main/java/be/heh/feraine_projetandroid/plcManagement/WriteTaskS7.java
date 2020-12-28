package be.heh.feraine_projetandroid.plcManagement;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import be.heh.feraine_projetandroid.Simatic_S7.S7;
import be.heh.feraine_projetandroid.Simatic_S7.S7Client;

public class WriteTaskS7
{
    /** ======== Attributs ======== **/
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private AutomateS7 plcS7;
    private Thread writeThread;

    private S7Client comS7;

    private String[] parConnexion = new String[10];
    private byte[] motCommande = new byte[256];

    /** ======== Constructeur ======== **/
    public WriteTaskS7()
    {
        this.comS7 = new S7Client();
        this.plcS7 = new AutomateS7();

        this.writeThread = new Thread(plcS7);
    }

    /** ======== Méthodes ======== **/
    // ======== Start ========
    public void Start(String ip, String rack, String slot)
    {
        if(!writeThread.isAlive())
        {
            this.parConnexion[0] = ip;
            this.parConnexion[1] = rack;
            this.parConnexion[2] = slot;

            this.writeThread.start();
            this.isRunning.set(true);
        }
    }

    // ======== Stop ========
    public void Stop()
    {
        this.isRunning.set(false);
        this.comS7.Disconnect();
        this.writeThread.interrupt();
    }

    // ======== WRITE ========
    // ==== BOOL ====
    public void setWriteBool(int by, int b, int v)
    {
        if(v == 1)
        {
            motCommande[by] = (byte) (b | motCommande[by]);
        }
        else
        {
            motCommande[by] = (byte) (~b & motCommande[by]);
        }
    }

    // ==== BYTE ====
    public void setWriteByte(int p, int v)
    {
        String s = Integer.toBinaryString(v);
        ArrayList<Boolean> booleans = new ArrayList<>();

        for(char c : s.toCharArray())
        {
            if(c == '1')
            {
                booleans.add(true);
            }
            else if(c == '0')
            {
                booleans.add(false);
            }
        }

        // Writre right --> left
        int i = booleans.size() - 1;

        for (Boolean b : booleans)
        {
            System.out.print(b + " ");
            S7.SetBitAt(motCommande, p, i, b);
            i--;
        }
    }

    // ==== INT ====
    public void setWriteInt(int p, int v)
    {
        S7.SetWordAt(motCommande, p, v);
    }

    /** ======== Classe interne ======== **/
    // ======== AutomateS7 ========
    private class AutomateS7 implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                comS7.SetConnectionType(S7.S7_BASIC);

                Integer res = comS7.ConnectTo(parConnexion[0], Integer.valueOf(parConnexion[1]), Integer.valueOf(parConnexion[2]));

                comS7.ReadArea(S7.S7AreaDB, 5, 0, 34, motCommande);

                while(isRunning.get() && (res.equals(0)))
                {
                    Integer writePLC = comS7.WriteArea(S7.S7AreaDB, 5, 0, 34, motCommande);

                    if(writePLC.equals(0))
                    {
                        Log.i("ret WRITE : ", String.valueOf(res) + "****" + String.valueOf(writePLC));
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
