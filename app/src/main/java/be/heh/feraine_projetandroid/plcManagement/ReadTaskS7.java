package be.heh.feraine_projetandroid.plcManagement;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

import be.heh.feraine_projetandroid.Simatic_S7.S7;
import be.heh.feraine_projetandroid.Simatic_S7.S7Client;
import be.heh.feraine_projetandroid.Simatic_S7.S7OrderCode;

public class ReadTaskS7
{
    /** ======== Attributs ======== **/
    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private AutomateS7 plcS7;
    private Thread readThread;

    private S7Client comS7;

    private String[] param = new String[10];
    private byte[] datasPLC = new byte[512];

    // ==== Layout ====
    // Packaging
    private TextView tv_packaging_title;
    private TextView tv_packaging_isActive;
    private TextView tv_packaging_nbrBottles;

    // Liquid Regulation
    private TextView tv_liquidRegulation_title;
    private TextView tv_liquidRegulation_manualAutoMode;

    /** ======== Constructeur ======== **/
    // ==== Packaging ====
    public ReadTaskS7(TextView tv_packaging_title, TextView tv_packaging_isActive, TextView tv_packaging_nbrBottles)
    {
        // TODO
        // ==== Layout ====
        // Packaging
        this.tv_packaging_title = tv_packaging_title;
        this.tv_packaging_isActive = tv_packaging_isActive;
        this.tv_packaging_nbrBottles = tv_packaging_nbrBottles;

        this.comS7 = new S7Client();
        this.plcS7 = new AutomateS7();

        this.readThread = new Thread(plcS7);
    }

    // ==== Liquid Regulation ====
    public ReadTaskS7(TextView tv_liquidRegulation_title , TextView tv_liquidRegulation_manualAutoMode)
    {
        // Liquid Regulation
        this.tv_liquidRegulation_title = tv_liquidRegulation_title;
        this.tv_liquidRegulation_manualAutoMode = tv_liquidRegulation_manualAutoMode;

        this.comS7 = new S7Client();
        this.plcS7 = new AutomateS7();

        this.readThread = new Thread(plcS7);
    }

    /** ======== Méthodes ======== **/
    // ======== Start ========
    public void Start(String ip, String rack, String slot)
    {
        if(!readThread.isAlive())
        {
            this.param[0] = ip;
            this.param[1] = rack;
            this.param[2] = slot;

            this.readThread.start();
            this.isRunning.set(true);
        }
    }

    // ======== Stop ========
    public void Stop()
    {
        this.isRunning.set(false);
        this.comS7.Disconnect();
        this.readThread.interrupt();
    }

    // ======== downloadOn...Execute ========
    // ==== downloadOnPreExecute ====
    private void downloadOnPreExecute(int t)
    {
        // VOID
    }

    // ==== downloadOnProgressExecute ====
    private void downloadOnProgressUpdate(int progress)
    {
        // == Packaging ==
        if(tv_packaging_title.getText().equals("Packaging"))
        {
            // Is Active ?
            if(S7.GetBitAt(datasPLC, 0, 0))
            {
                tv_packaging_isActive.setText("ON");
            }
            else
            {
                tv_packaging_isActive.setText("OFF");
            }

            // TODO
            // Number of bottles
            tv_packaging_nbrBottles.setText(String.valueOf(S7.GetWordAt(datasPLC, 16)));

            // Number of pills per bottles
        }

        // == Liquid Regulation ==
        else if(tv_liquidRegulation_title.getText().equals("Liquid Regulation"))
        {
            // Mode
            if(S7.GetBitAt(datasPLC,0, 5))
            {
                tv_liquidRegulation_manualAutoMode.setText("Auto");
            }
            else
            {
                tv_liquidRegulation_manualAutoMode.setText("Manual");
            }
        }
    }

    // ==== downloadOnPostExecute ====
    private void downloadOnPostExecute()
    {
        // TODO
    }

    /** ======== Classe anonyme ======== **/
    // ======== Handler ========
    private Handler monHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            switch(msg.what)
            {
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute(msg.arg1);

                    break;

                case MESSAGE_PROGRESS_UPDATE:
                    downloadOnProgressUpdate(msg.arg1);

                    break;

                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();

                    break;

                default:
                    break;
            }
        }
    };

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

                Integer res = comS7.ConnectTo(param[0],Integer.valueOf(param[1]),Integer.valueOf(param[2]));
                S7OrderCode orderCode = new S7OrderCode();
                Integer result = comS7.GetOrderCode(orderCode);

                int numCPU = -1;

                if(res.equals(0) && result.equals(0))
                {
                    numCPU = Integer.valueOf(orderCode.Code().toString().substring(5, 8));
                }
                else numCPU = 0000;

                sendPreExecuteMessage(numCPU);

                while(isRunning.get())
                {
                    if(res.equals(0))
                    {
                        int retInfo = comS7.ReadArea(S7.S7AreaDB, 5, 0, 34, datasPLC);
                        int data = 0;

                        if(retInfo == 0)
                        {
                            data = S7.GetWordAt(datasPLC, 0);
                            sendProgressMessage(data);
                        }
                    }

                    try
                    {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                sendPostExecuteMessage();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        // ======== send...ExecuteMessage ========
        // ==== sendPreExecuteMessage ====
        private void sendPreExecuteMessage(int v)
        {
            Message preExecuteMsg = new Message();

            preExecuteMsg.what = MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1 = v;
            monHandler.sendMessage(preExecuteMsg);
        }

        // ==== sendProgressMessage ====
        private void sendProgressMessage(int i)
        {
            Message progressMsg = new Message();

            progressMsg.what = MESSAGE_PROGRESS_UPDATE;
            progressMsg.arg1 = i;
            monHandler.sendMessage(progressMsg);
        }

        // ==== sendPostExecuteMessage ====
        private void sendPostExecuteMessage()
        {
            Message postExecuteMsg = new Message();

            postExecuteMsg.what = MESSAGE_POST_EXECUTE;
            monHandler.sendMessage(postExecuteMsg);
        }
    }
}
