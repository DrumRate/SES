package ses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hypothesi
{
    @SerializedName("statrWith")
    @Expose
    public Double statrWith;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("hypothesiss")
    @Expose
    public List<Hypothesis> hypothesiss = null;
}
