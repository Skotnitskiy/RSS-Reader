package com.itcuties.android.reader.services;
import android.app.Service; 
import android.content.Intent;
import android.os.IBinder; 
import android.widget.Toast; 

public class UpdateRssService extends Service 
{ 
   
    @Override 
    public IBinder onBind(Intent intent) 
	{ 
        return null; 
    } 

    @Override 
    public void onCreate() 
	{ 
        Toast.makeText(this, "Служба создана", 
            Toast.LENGTH_SHORT).show(); 
    } 
	
    @Override 
    public void onStart(Intent intent, int startid) 
	{
	    Toast.makeText(this, "Служба запущена", 
            Toast.LENGTH_SHORT).show(); 
       
    } 
	
    @Override 
    public void onDestroy() 
	{ 
        Toast.makeText(this, "Служба остановлена", 
            Toast.LENGTH_SHORT).show(); 
       
    } 
}
