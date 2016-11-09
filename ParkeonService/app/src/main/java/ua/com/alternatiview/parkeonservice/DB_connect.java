package ua.com.alternatiview.parkeonservice;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;


/**
 * Created by Sorrow on 02.11.2016.
 */

//TODO Модифицировать поиск для Like
public class DB_connect {

    //Метод взять данные определенного девайса из базы
    public Device GetDevice(String machineName) {
        BufferedReader stringToReceive;
        Device machine = new Device(machineName);
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/get_device.php?name=" + URLEncoder.encode(machineName, "UTF-8");
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray device = jsonObject.getJSONArray("Device");
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        for (int i = 0; i < device.length(); i++) {
                            String machineID = device.getJSONObject(i).getString("Name");
                            Double longitude = device.getJSONObject(i).getDouble("Longitude");
                            Double latitude = device.getJSONObject(i).getDouble("Latitude");
                            int status = device.getJSONObject(i).getInt("Status");
                            machine = new Device(machineID, longitude, latitude, status);
                        }
                    } else {
                        //TODO Show - jsonObj.getString("message")
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."
                    //String res = "Error parsing JSON data.";
                    //Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
        return machine;
    }

    //Метод собрать девайсы по критерию Status
    public LinkedList<Device> GetSelectedDevices(int select) {
        LinkedList<Device> machineList = new LinkedList<>();
        BufferedReader stringToReceive;
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/get_all_devices.php?Status=" + String.valueOf(select);
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray device = jsonObject.getJSONArray("Device");
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        for (int i = 0; i < device.length(); i++) {
                            String machineID = device.getJSONObject(i).getString("Name");
                            Double longitude = device.getJSONObject(i).getDouble("Longitude");
                            Double latitude = device.getJSONObject(i).getDouble("Latitude");
                            int status = device.getJSONObject(i).getInt("Status");
                            machineList.add(new Device(machineID, longitude, latitude, status));
                        }
                    } else {
                        //TODO Check if it's working.
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Check if it's working
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

        return machineList;
    }

    // Метод забрать все девайсы из базы
    public LinkedList<Device> GetAllDevices() {
        LinkedList<Device> devicesList = new LinkedList<>();
        BufferedReader stringToReceive;
        String link = "http://parkeon.alternatiview.com.ua/backoffice/get_all_devices.php?";
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray device = jsonObject.getJSONArray("Device");
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        for (int i = 0; i < device.length(); i++) {
                            String machineID = device.getJSONObject(i).getString("Name");
                            Double longitude = device.getJSONObject(i).getDouble("Longitude");
                            Double latitude = device.getJSONObject(i).getDouble("Latitude");
                            int status = device.getJSONObject(i).getInt("Status");
                            devicesList.add(new Device(machineID, longitude, latitude, status));
                        }
                    } else {
                        //TODO Check if it's working.
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Check if it's working
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
        return devicesList;
    }

    // Создание временной таблицы в БД
    public void CreateTempTable(String userID) {
        BufferedReader stringToReceive;
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/create_temp_table.php?TableName=" + URLEncoder.encode(userID, "UTF-8");
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                    } else {
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }

    // Удаление временной таблицы в БД
    public void DropTempTable(String userID) {
        BufferedReader stringToReceive;
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/drop_temp_table.php?TableName=" + userID;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {

                    } else {
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }

    // Забрать все данные из временной таблицы
    LinkedList<Device> GetTempDevices(String androidID) {
        LinkedList<Device> machineList = new LinkedList<>();
        BufferedReader stringToReceive;
        String link = "http://parkeon.alternatiview.com.ua/backoffice/get_devices_temp_table.php?TempTable=" + androidID;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray device = jsonObject.getJSONArray("Device");
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        for (int i = 0; i < device.length(); i++) {
                            String machineID = device.getJSONObject(i).getString("Name");
                            Double longitude = device.getJSONObject(i).getDouble("Longitude");
                            Double latitude = device.getJSONObject(i).getDouble("Latitude");
                            int status = device.getJSONObject(i).getInt("Status");
                            machineList.add(new Device(machineID, longitude, latitude, status));
                        }
                    } else {
                        //TODO Check if it's working.
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Check if it's working
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

        return machineList;
    }

    // Заполнить временную таблицу
    public void InsertToTempTable(String userID, int status) {
        String addList = "TempTable=" + userID + "&Status=" + Integer.toString(status);
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/insert_into_temp_table.php?" + addList;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        //TODO something to show
                        Toast.makeText(MainActivity.context, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

    }

    // Заполнить временную таблицу, перегруженый
    public void InsertToTempTable(String userID) {
        String addList = "TempTable=" + userID;
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/insert_all_into_temp_table.php?" + addList;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        //TODO something to show
                        Toast.makeText(MainActivity.context, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

    }

    // Заполнить временную таблицу, перегруженый
    public void InsertToTempTable(String userID, String machineName) {
        String addList = "TempTable=" + userID + "&Name=" + machineName;
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/insert_one_into_temp_table.php?" + addList;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        //TODO something to show
                        Toast.makeText(MainActivity.context, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

    }

    // Добавить новый девайс в основную таблицу
    public void InsertNewDevice(Device machine) {
        String addList = "Name=" + machine.machineID + "&Longitude=" + String.valueOf(machine.longitude) + "&Latitude=" + String.valueOf(machine.latitude) + "&Status=" + String.valueOf(machine.status);
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/create_new_device.php?" + addList;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        //TODO something to show
                        Toast.makeText(MainActivity.context, "New machine successfully added", Toast.LENGTH_SHORT).show();
                    } else {
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }

    //Обновить статус девайса
    public void UpdateStatus(String machineName, int status) {
        String addList = "Name=" + machineName + "&Status=" + String.valueOf(status);
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/update_device_status.php?" + addList;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        //TODO something to show
                        Toast.makeText(MainActivity.context, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }

    //Обновить координаты девайса
    public void UpdateLocation(String machineName, Double longitude, Double latitude) {
        String addList = "Name=" + machineName + "&Longitude=" + String.valueOf(longitude) + "&Latitude=" + String.valueOf(latitude);
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/update_device_location.php?" + addList;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        //TODO something to show
                        Toast.makeText(MainActivity.context, "Location updated", Toast.LENGTH_SHORT).show();
                    } else {
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteDevice(String machineName) {
        String addList = "Name=" + machineName;
        try {
            String link = "http://parkeon.alternatiview.com.ua/backoffice/delete_device.php?" + addList;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader stringToReceive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReceive.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        //TODO something to show
                        Toast.makeText(MainActivity.context, "Machine deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }
}
