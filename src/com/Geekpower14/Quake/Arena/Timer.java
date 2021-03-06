package com.Geekpower14.Quake.Arena;

import com.Geekpower14.Quake.Quake;
import com.Geekpower14.Quake.Versions.GetSounds;
import com.Geekpower14.Quake.Versions.SelectVersion;
import org.bukkit.Sound;

public class Timer implements Runnable {
    public Quake _plugin;
    public Arena _arena;
    public int _finish = 15;

    public Timer(Quake pl, Arena aren) {
        _plugin = pl;
        _arena = aren;
    }

    @Override
    public void run() {
        Sound _sound = GetSounds.GetNotePling();
        
        if(_plugin.getServer().getOnlinePlayers().isEmpty())
            return;

        if(_arena._etat <= _arena._starting && _arena._etat != 0) {
            _arena._etat--;
            _arena.broadcastXP(_arena._etat);
        }
        
        if(_arena._etat == 10)
            _arena.broadcast(_plugin._trad.get("Game.Arena.Message.RemainTime").replace("[TIME]", "10"));

        if(_arena._etat <= 5 && _arena._etat >= 1) {
            _arena.broadcast(_plugin._trad.get("Game.Arena.Message.RemainTime").replace("[TIME]", "" + _arena._etat));
            if(_sound != null)
                _arena.playsound(_sound, 0.6f, 50.0f);

        }
        
        if (_arena._etat == 0) {
            if(_sound != null) {
                _arena.playsound(_sound, 9.0f, 1.0f);
                _arena.playsound(_sound, 9.0f, 5.0f);
                _arena.playsound(_sound, 9.0f, 10.0f);
            }

            _arena.broadcast(_plugin._trad.get("Game.Arena.Message.Start"));
            _arena.start();
            _arena.nbroadcast("");
            _arena.nbroadcast(_plugin._trad.get("Game.Arena.Message.Start-Info"));
        }
    }
}

