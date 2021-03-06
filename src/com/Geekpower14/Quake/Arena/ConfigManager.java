package com.Geekpower14.Quake.Arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    private YamlConfiguration _cfg = new YamlConfiguration();
    private File _configFile;
    private Map<String, Boolean> _booleans;
    private Map<String, Integer> _ints;
    private Map<String, Double> _doubles;
    private Map<String, String> _strings;

    public ConfigManager(File configFile) {
        _configFile = configFile;
        _booleans = new HashMap<>();
        _ints = new HashMap<>();
        _doubles = new HashMap<>();
        _strings = new HashMap<>();
    }

    public void createDefaults() {
        _cfg.options().indent(4);
        for(ACFG cfg: ACFG.values())
            _cfg.addDefault(cfg.getNode(), cfg.getValue());
        save();
    }

    public boolean load() {
        try {
            _cfg.load(_configFile);
            reloadMaps();
            return true;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void reloadMaps() {
        for(String s : _cfg.getKeys(true)) {
            Object object = _cfg.get(s);
            if(object instanceof Boolean) {
                _booleans.put(s, (Boolean)object);
                continue;
            }
            if(object instanceof Integer) {
                _ints.put(s, (Integer)object);
                continue;
            }
            if(object instanceof Double) {
                _doubles.put(s, (Double)object);
                continue;
            }
            if(!(object instanceof String))
                continue;
            _strings.put(s, (String)object);
        }
    }

    public boolean save() {
        try {
            _cfg.save(_configFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete() {
        return _configFile.delete();
    }

    public void setHeader(String header) {
        _cfg.options().header(header);
    }

    public YamlConfiguration getYamlConfiguration() {
        return _cfg;
    }

    public Object getUnsafe(String string) {
        return _cfg.get(string);
    }

    public boolean getBoolean(ACFG cfg) {
        return getBoolean(cfg, (Boolean)cfg.getValue());
    }

    private boolean getBoolean(ACFG cfg, boolean def) {
        String path = cfg.getNode();
        Boolean result = _booleans.get(path);
        return result == null ? def : result;
    }

    public int getInt(ACFG cfg) {
        return getInt(cfg, (Integer)cfg.getValue());
    }

    public int getInt(ACFG cfg, int def) {
        String path = cfg.getNode();
        Integer result = _ints.get(path);
        return result == null ? def : result;
    }

    public double getDouble(ACFG cfg) {
        return getDouble(cfg, (Double)cfg.getValue());
    }

    public double getDouble(ACFG cfg, double def) {
        String path = cfg.getNode();
        Double result = _doubles.get(path);
        return result == null ? def : result;
    }

    public String getString(ACFG cfg) {
        return getString(cfg, (String)cfg.getValue());
    }

    public String getString(ACFG cfg, String def) {
        String path = cfg.getNode();
        String result = _strings.get(path);
        return result == null ? def : result;
    }

    public Set<String> getKeys(String path) {
        if(_cfg.get(path) == null) {
            return null;
        }
        ConfigurationSection section = _cfg.getConfigurationSection(path);
        return section.getKeys(false);
    }

    public List<String> getStringList(String path, List<String> def) {
        if(_cfg.get(path) == null) {
            return def == null ? new LinkedList() : def;
        }
        return _cfg.getStringList(path);
    }

    public void setManually(String path, Object value) {
        if(value instanceof Boolean) {
            _booleans.put(path, (Boolean)value);
        } else if (value instanceof Integer) {
            _ints.put(path, (Integer)value);
        } else if (value instanceof Double) {
            _doubles.put(path, (Double)value);
        } else if (value instanceof String) {
            _strings.put(path, (String)value);
        } else 
            _cfg.set(path, value);
    }

    public void set(ACFG cfg, Object value) {
        setManually(cfg.getNode(), value);
    }

    public Location str2loc(String loc) {
        if (loc == null) {
            return null;
        }
        Location res = null;
        String[] loca = loc.split(", ");
        res = new Location(Bukkit.getServer().getWorld(loca[0]), Double.parseDouble(loca[1]), Double.parseDouble(loca[2]), Double.parseDouble(loca[3]), Float.parseFloat(loca[4]), Float.parseFloat(loca[5]));
        return res;
    }

    public static enum ACFG {
        Z("configversion", "v0.9.0.0"),
        Spawns("Nombre", new ArrayList()),
        CHAT_DEFAULTTEAM("chat.defaultTeam", Boolean.valueOf(false)),
        MODULES_WORLDEDIT_AUTOLOAD("modules.worldedit.autoload", Boolean.valueOf(false)),
        MODULES_WORLDEDIT_AUTOSAVE("modules.worldedit.autosave", Boolean.valueOf(false));
        
        private String _node;
        private final Object _value;
        private final String _type;

        public static ACFG getByNode(String node) {
            ACFG[] arraCFG = ACFG.values();
            int n = arraCFG.length;
            int n2 = 0;
            while (n2 < n) {
                ACFG m = arraCFG[n2];
                if (m.getNode().equals(node)) {
                    return m;
                }
                ++n2;
            }
            return null;
        }

        private ACFG(String node, String s) {
            _node = node;
            _value = s;
            _type = "string";
        }

        private ACFG(String node, Boolean b) {
            _node = node;
            _value = b;
            _type = "boolean";
        }

        private ACFG(String node, Integer i) {
            _node = node;
            _value = i;
            _type = "int";
        }

        private ACFG(String node, Double d) {
            _node = node;
            _value = d;
            _type = "double";
        }

        private ACFG(String node, List<String> value) {
            _node = node;
            _value = value;
            _type = "list";
        }

        public String getNode() {
            return _node;
        }

        public void setNode(String value) {
            _node = value;
        }

        @Override
        public String toString() {
            return String.valueOf(_value);
        }

        public Object getValue() {
            return _value;
        }

        public static ACFG[] getValues() {
            return ACFG.values();
        }

        public String getType() {
            return _type;
        }
    }
}

