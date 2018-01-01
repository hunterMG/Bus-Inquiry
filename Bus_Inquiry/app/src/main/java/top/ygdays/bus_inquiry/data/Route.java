package top.ygdays.bus_inquiry.data;

import java.sql.Time;
import java.util.List;

/**
 * @Author: Guang Yan
 * @Description:
 * @Date: Created in 上午10:48 2017/12/31
 */
public class Route {
    public int RouteID;
    public String RouteName;
    public Double Price;
    public String StartTime;
    public String EndTime;
    public String StartStop;
    public String EndStop;
    public int StopNum;
    public List<String> Stops;
    public Route(int RouteID, String RouteName, Double Price, String StartTime, String EndTime, String StartStop, String EndStop, int StopNum, List<String> Stops){
        this.RouteID = RouteID;
        this.RouteName = RouteName;
        this.Price = Price;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.StartStop = StartStop;
        this.EndStop = EndStop;
        this.StopNum = StopNum;
        this.Stops = Stops;
    }

    @Override
    public String toString() {
        return "Route:"+"RouteID-"+this.RouteID+", RouteName-"+RouteName+", Price-"+Price+", StartTime-"+StartTime+", EndTime-"+EndTime;
    }

    public String getStops(){
        return Stops.toString();
    }
}
