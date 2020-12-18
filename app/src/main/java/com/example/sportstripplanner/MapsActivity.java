package com.example.sportstripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private DatePickerDialog datePicker;
    private EditText editTextDateFrom;
    private EditText editTextDateTo;
    private HashMap<String, Team> teams;
    private Vector<Game> games = new Vector<Game>();
    private Button filterTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        filterTextButton = findViewById(R.id.filterButton);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(null);
        mapView.getMapAsync(this);

        editTextDateFrom=(EditText) findViewById(R.id.editTextDateFrom);
        editTextDateFrom.setInputType(InputType.TYPE_NULL);
        editTextDateFrom.setOnClickListener(v -> {
            ShowDatePicker(editTextDateFrom);
        });

        editTextDateTo=(EditText) findViewById(R.id.editTextDateTo);
        editTextDateTo.setInputType(InputType.TYPE_NULL);
        editTextDateTo.setOnClickListener(v -> {
            ShowDatePicker(editTextDateTo);
        });

        teams = CreateHockeyTeams();

        Team homeTeam;
        Team awayTeam;
        Date datePlayed;

        //parse csv
        InputStream is = getResources().openRawResource(R.raw.hockeygames);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                datePlayed = new SimpleDateFormat("yyyy-MM-dd").parse(tokens[0]);
                homeTeam = teams.get(tokens[3]);
                awayTeam = teams.get(tokens[1]);
                games.add(new Game(datePlayed, homeTeam, awayTeam));
            }
        } catch (IOException | ParseException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }

        //add on click for button to filter the csv
        filterTextButton.setOnClickListener(v -> {
            try {
                Date from = new SimpleDateFormat("dd/MM/yyyy").parse(editTextDateFrom.getText().toString());
                Date to = new SimpleDateFormat("dd/MM/yyyy").parse(editTextDateTo.getText().toString());

                if(from != null && to != null)
                {
                    mMap.clear();
                    Game game;
                    for(int i = 0; i < games.size(); ++i)
                    {
                        game = games.get(i);
                        if(game.datePlayed.compareTo(from) >= 0 && game.datePlayed.compareTo(to) <= 0)
                        {
                            //add new markers based on games in that range
                            mMap.addMarker(new MarkerOptions().position(game.homeTeam.arenaPosition).title(game.homeTeam.arenaName).icon(game.homeTeam.teamIcon));
                        }
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add markers
        Iterator teamsIterator = teams.entrySet().iterator();
        while (teamsIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) teamsIterator.next();
            Team team = (Team)mapElement.getValue();
            mMap.addMarker(new MarkerOptions().position(team.arenaPosition).title(team.arenaName).icon(team.teamIcon));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(teams.get(getString(R.string.anaheim)).arenaPosition));
    }

    public HashMap<String, Team> CreateHockeyTeams()
    {
        HashMap<String, Team> hockeyTeams = new HashMap<String, Team>();

        hockeyTeams.put(getString(R.string.anaheim), new Team(getString(R.string.anaheim), new LatLng(33.8078476, -117.8764687),"Honda Center", R.drawable.anaheim_ducks));
        hockeyTeams.put(getString(R.string.arizona), new Team(getString(R.string.arizona), new LatLng(33.532074, -112.2612163),"Gila River Arena", R.drawable.arizona_coyotes));
        hockeyTeams.put(getString(R.string.boston), new Team(getString(R.string.boston), new LatLng(42.366198, -71.062146),"TD Garden", R.drawable.boston_bruins));
        hockeyTeams.put(getString(R.string.buffalo), new Team(getString(R.string.buffalo), new LatLng(42.8754393, -78.8764375),"Key Bank Center", R.drawable.buffalo_sabres));
        hockeyTeams.put(getString(R.string.calgary), new Team(getString(R.string.calgary), new LatLng(51.0374336, -114.0519341),"Scotiabank Saddledome", R.drawable.calgary_flames));
        hockeyTeams.put(getString(R.string.carolina), new Team(getString(R.string.carolina), new LatLng(35.803329, -78.7218018),"PNC Arena", R.drawable.carolina_hurricanes));
        hockeyTeams.put(getString(R.string.chicago), new Team(getString(R.string.chicago), new LatLng(41.8806908, -87.6741759),"United Center", R.drawable.chicago_blackhawks));
        hockeyTeams.put(getString(R.string.colorado), new Team(getString(R.string.colorado), new LatLng(39.7486565,-105.0076028),"Ball Arena", R.drawable.colorado_avalanche));
        hockeyTeams.put(getString(R.string.columbus), new Team(getString(R.string.columbus), new LatLng(39.9692072, -83.0060483),"Nationwide Arena", R.drawable.columbus_blue_jackets));
        hockeyTeams.put(getString(R.string.dallas), new Team(getString(R.string.dallas), new LatLng(32.7903143, -96.810148),"American Airlines Center", R.drawable.dallas_stars));
        hockeyTeams.put(getString(R.string.detroit), new Team(getString(R.string.detroit), new LatLng(42.3411026, -83.0552673),"Little Caesars Arena", R.drawable.detroit_red_wings));
        hockeyTeams.put(getString(R.string.edmonton), new Team(getString(R.string.edmonton), new LatLng(53.546236, -113.497221),"Rogers Arena", R.drawable.edmonton_oilers));
        hockeyTeams.put(getString(R.string.florida), new Team(getString(R.string.florida), new LatLng(26.1584958, -80.3255204),"BB&T Center", R.drawable.florida_panthers));
        hockeyTeams.put(getString(R.string.losAngeles), new Team(getString(R.string.losAngeles), new LatLng(34.0430175, -118.2672541),"STAPLES Center", R.drawable.los_angeles_kings));
        hockeyTeams.put(getString(R.string.minnesota), new Team(getString(R.string.minnesota), new LatLng(44.9448315, -93.101097),"Xcel Energy Center", R.drawable.minnesota_wild));
        hockeyTeams.put(getString(R.string.montreal), new Team(getString(R.string.montreal), new LatLng(45.4960667, -73.5693153),"Centre Bell", R.drawable.montreal_canadiens));
        hockeyTeams.put(getString(R.string.nashville), new Team(getString(R.string.nashville), new LatLng(36.1591736, -86.7784959),"Bridgestone Arena", R.drawable.nashville_predators));
        hockeyTeams.put(getString(R.string.newJersey), new Team(getString(R.string.newJersey), new LatLng(40.718031, -74.149678),"Prudential Center", R.drawable.new_jersey_devils));
        hockeyTeams.put(getString(R.string.newYorkI), new Team(getString(R.string.newYorkI), new LatLng(40.7232778, -73.5906528),"Nassau Coliseum", R.drawable.new_york_islanders));
        hockeyTeams.put(getString(R.string.newYorkR), new Team(getString(R.string.newYorkR), new LatLng(40.7505045, -73.9934387),"Madison Square Garden", R.drawable.new_york_rangers));
        hockeyTeams.put(getString(R.string.ottawa), new Team(getString(R.string.ottawa), new LatLng(45.2969151, -75.926823),"Canadian Tire Centre", R.drawable.ottawa_senators));
        hockeyTeams.put(getString(R.string.philadelphia), new Team(getString(R.string.philadelphia), new LatLng(39.9012015, -75.1719795),"Wells Fargo Center", R.drawable.philadelphia_flyers));
        hockeyTeams.put(getString(R.string.pittsburgh), new Team(getString(R.string.pittsburgh), new LatLng(40.4393097, -79.9895529),"PPG Paints Arena", R.drawable.pittsburgh_penguins));
        hockeyTeams.put(getString(R.string.sanJose), new Team(getString(R.string.sanJose), new LatLng(37.3327507, -121.9012447),"SAP Center at San Jose", R.drawable.san_jose_sharks));
        hockeyTeams.put(getString(R.string.stLouis), new Team(getString(R.string.stLouis), new LatLng(38.6268402, -90.2026783),"Enterprise Center", R.drawable.st_louis_blues));
        hockeyTeams.put(getString(R.string.tampa), new Team(getString(R.string.tampa), new LatLng(27.9427373, -82.4517771),"Amalie Arena", R.drawable.tampa_bay_lightning));
        hockeyTeams.put(getString(R.string.toronto), new Team(getString(R.string.toronto), new LatLng(43.6434661, -79.3790989),"Scotiabank Arena", R.drawable.toronto_maple_leafs));
        hockeyTeams.put(getString(R.string.vancouver), new Team(getString(R.string.vancouver), new LatLng(49.2778358, -123.1088227),"Rogers Arena", R.drawable.vancouver_canucks));
        hockeyTeams.put(getString(R.string.vegas), new Team(getString(R.string.vegas), new LatLng(36.1028767,-115.1784324),"T-Mobile Arena", R.drawable.vegas_golden_knights));
        hockeyTeams.put(getString(R.string.washington), new Team(getString(R.string.washington), new LatLng(38.8980608, -77.0208009),"Capital One Arena", R.drawable.washington_capitals));
        hockeyTeams.put(getString(R.string.winnipeg), new Team(getString(R.string.winnipeg), new LatLng(49.8927149,-97.1437517),"Bell MTS Place", R.drawable.winnipeg_jets));

        return hockeyTeams;
    }

    public void ShowDatePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        // date picker dialog
        datePicker = new DatePickerDialog(MapsActivity.this,
                //Lambda to set the text after setting the date
                (view, year1, monthOfYear, dayOfMonth) -> editText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), 2018, 9, 3); //normally wouldn't hardcode this but for this example where the season is 2018/2019
        datePicker.show();
    }


}