package be.heh.feraine_projetandroid.plcManagement;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
    private byte[] datasPLC = new byte[256];

    // ==== Layout ====
    private TextView tv_title;

    // Packaging
    private TextView tv_packaging_isActive;
    private TextView tv_packaging_nbrBottles;
    private TextView tv_packaging_nbrPillsPerBottles;
    private TextView tv_packaging_askForPills;
    private TextView tv_packaging_selector;
    private TextView tv_packaging_bottlesGenerator;

    // Liquid Regulation
    private TextView tv_liquidRegulation_liquidLvl;
    private TextView tv_liquidRegulation_manualAutoMode;
    private TextView tv_liquidRegulation_setpoint;
    private TextView tv_liquidRegulation_manualValue;
    private TextView tv_liquidRegulation_outputValue;

    /** ======== Constructeur ======== **/
    // ==== Packaging ====
    public ReadTaskS7(TextView tv_title, TextView tv_packaging_isActive, TextView tv_packaging_nbrBottles, TextView tv_packaging_nbrPillsPerBottles,
                      TextView tv_packaging_askForPills, TextView tv_packaging_selector, TextView tv_packaging_bottlesGenerator)
    {
        this.tv_title = tv_title;
        this.tv_packaging_isActive = tv_packaging_isActive;
        this.tv_packaging_nbrBottles = tv_packaging_nbrBottles;
        this.tv_packaging_nbrPillsPerBottles = tv_packaging_nbrPillsPerBottles;
        this.tv_packaging_askForPills = tv_packaging_askForPills;
        this.tv_packaging_selector = tv_packaging_selector;
        this.tv_packaging_bottlesGenerator = tv_packaging_bottlesGenerator;

        this.comS7 = new S7Client();
        this.plcS7 = new AutomateS7();

        this.readThread = new Thread(plcS7);
    }

    // ==== Liquid Regulation ====
    public ReadTaskS7(TextView tv_title, TextView tv_liquidRegulation_liquidLvl, TextView tv_liquidRegulation_manualAutoMode,
                      TextView tv_liquidRegulation_setpoint, TextView tv_liquidRegulation_manualValue, TextView tv_liquidRegulation_outputValue)
    {
        this.tv_title = tv_title;
        this.tv_liquidRegulation_liquidLvl = tv_liquidRegulation_liquidLvl;
        this.tv_liquidRegulation_manualAutoMode = tv_liquidRegulation_manualAutoMode;
        this.tv_liquidRegulation_setpoint = tv_liquidRegulation_setpoint;
        this.tv_liquidRegulation_manualValue = tv_liquidRegulation_manualValue;
        this.tv_liquidRegulation_outputValue = tv_liquidRegulation_outputValue;

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
    @SuppressLint("SetTextI18n")
    private void downloadOnProgressUpdate(int progress)
    {
        // == Packaging ==
        if(tv_title.getText().equals("Packaging"))
        {
            // Is active ?
            if(S7.GetBitAt(datasPLC, 4, 1))
            {
                tv_packaging_isActive.setText("Motor : ON");
            }
            else
            {
                tv_packaging_isActive.setText("Motor : OFF");
            }

            // Selector in service ?
            if(S7.GetBitAt(datasPLC, 0, 0))
            {
                tv_packaging_selector.setText("Selector : ON");
            }
            else
            {
                tv_packaging_selector.setText("Selector : OFF");
            }

            // Bottles generator
            if(S7.GetBitAt(datasPLC, 1, 3))
            {
                tv_packaging_bottlesGenerator.setText("Arrival of empty bottles : ON");
            }
            else
            {
                tv_packaging_bottlesGenerator.setText("Arrival of empty bottles : OFF");
            }

            // Number of bottles
            tv_packaging_nbrBottles.setText("Number of bottles : " + S7.GetWordAt(datasPLC, 16));

            // Number of pills per bottles
            tv_packaging_nbrPillsPerBottles.setText("Number of pills per bottles : " + S7.GetWordAt(datasPLC, 14));

            // Asking for x pills
            // 5 pills
            if(S7.GetBitAt(datasPLC, 4, 3))
            {
                tv_packaging_askForPills.setText("Asking for 5 pills");
            }
            // 10 pills
            else if(S7.GetBitAt(datasPLC, 4, 4))
            {
                tv_packaging_askForPills.setText("Asking for 10 pills");
            }
            // 15 pills
            else if(S7.GetBitAt(datasPLC, 4, 5))
            {
                tv_packaging_askForPills.setText("Asking for 15 pills");
            }
            else
            {
                tv_packaging_askForPills.setText("Asking for 0 pills");
            }
        }

        // == Liquid Regulation ==
        else if(tv_title.getText().equals("Liquid Regulation"))
        {
            // Liquid Level
            tv_liquidRegulation_liquidLvl.setText("Liquid level : " + S7.GetWordAt(datasPLC,16) + "l");

            // Mode
            if(S7.GetBitAt(datasPLC,0, 5))
            {
                tv_liquidRegulation_manualAutoMode.setText("Mode : Auto");
            }
            else
            {
                tv_liquidRegulation_manualAutoMode.setText("Mode : Manual");
            }

            // Setpoint
            tv_liquidRegulation_setpoint.setText("Setpoint : " + S7.GetWordAt(datasPLC,18));

            // Manual value
            tv_liquidRegulation_manualValue.setText("Manual value : " + S7.GetWordAt(datasPLC, 20));

            // Output value
            tv_liquidRegulation_outputValue.setText("Output value : " + S7.GetWordAt(datasPLC,22));
        }
    }

    // ==== downloadOnPostExecute ====
    private void downloadOnPostExecute()
    {
        // VOID
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
                // Log.i("Num", "Num : " + numCPU);

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

                    // Log.i("Connect ?", "Yes");

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
