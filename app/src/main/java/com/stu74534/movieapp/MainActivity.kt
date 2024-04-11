package com.stu74534.movieapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlin.random.Random

// --------------------------------------------------Movie Storage------------------------------------------------------------
class MovieViewModel : ViewModel() {

    private val _listOfMovies = mutableStateListOf<Movie>()
    val listOfMovies: List<Movie>
        get() = _listOfMovies

    init {

        var initialListOfMovies = listOf(
            Movie(
                name = "Superman",
                image = R.drawable.superman2,
                certification = "",
                description = "Christopher Reeve as Clark Kent / Superman: Born on Krypton as Kal-El and raised on Earth, he is a being of immense power, strength, flight and invulnerability who, after realizing his destiny to serve mankind, uses his powers to protect and save others..",
                starring = arrayOf("Christopher Reeve", "Marion Brando", "Gene Hackman"),
                running_time_mins = 143,
                seats_remaining = Random.nextInt(16),
                seats_selected = 0
            ),
            Movie(
                name = "Batman",
                image = R.drawable.batman2,
                certification = "",
                description = "Batman is the superhero protector of Gotham City, a tortured, brooding vigilante dressed as a bat who fights against evil and strikes fear into the hearts of criminals everywhere. In his public identity, he is Bruce Wayne, a billionaire industrialist and notorious playboy.",
                starring = arrayOf("Robert Pattinson", "Zoe Kravitz", "Paul Dano"),
                running_time_mins = 143,
                seats_remaining = Random.nextInt(16),
                seats_selected = 0
            ),Movie(
                name = "Infinity War",
                image = R.drawable.infinitywar,
                certification = "",
                description = "The Avengers and allies safeguard the world from immense threats, but a new danger arises: Thanos, a feared despot aiming to collect the six Infinity Stones, incredibly powerful artifacts.",
                starring = arrayOf("Robert Downey Jr", "Josh Brolin", "Scarlett Johnson"),
                running_time_mins = 149,
                seats_remaining = Random.nextInt(16),
                seats_selected = 0
            ),
            Movie(
                name = "SpiderMan",
                image = R.drawable.spiderman,
                certification = "",
                description = "Bitten by a radioactive spider, Peter Parker gains incredible arachnid abilities that he uses to aid others, all while facing personal challenges. With his amazing spider-like powers, teenage science prodigy Peter Parker fights crime and aspires to join the Avengers as Spider-Man",
                starring = arrayOf("Tom Holland", "Toby Maguire", "Drake Bell"),
                running_time_mins = 140,
                seats_remaining = Random.nextInt(16),
                seats_selected = 0
            ),


            )
        _listOfMovies.addAll(initialListOfMovies)
}

    fun updateMovieList(updatedMovies: List<Movie>) {
        _listOfMovies.clear()
        _listOfMovies.addAll(updatedMovies)
    }
}

// --------------------------------------------------Main Activity------------------------------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val movieViewModel: MovieViewModel = viewModel()
            NavHost(navController = navController, startDestination = "mainMenu") {
                composable("mainMenu") {
                    MainMenu(navController = navController,movieViewModel.listOfMovies,movieViewModel)
                    { updatedMovies ->
                        movieViewModel.updateMovieList(updatedMovies)
                    }
                }
                composable("movieScreen/{movieName}") { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val movieName = arguments.getString("movieName")
                    val movie = movieViewModel.listOfMovies.find { it.name == movieName }
                    if (movie != null) {
                        MovieScreen(navController = navController, movie = movie,movieViewModel.listOfMovies,movieViewModel)
                        { updatedMovies ->
                            movieViewModel.updateMovieList(updatedMovies)
                        }
                    } else {
                    }
                }
            }
        }
    }
}


// --------------------------------------------------Class Movie ------------------------------------------------------------

data class Movie(
    var name: String,
    var image: Int,
    val certification: String,
    val description: String,
    val starring: Array<String>,
    val running_time_mins: Int,
    val seats_remaining: Int,
    var seats_selected: Int
)

// --------------------------------------------------Main Menu  ------------------------------------------------------------

@Composable
fun MainMenu(
    navController: NavHostController,
    listOfMovies: List<Movie>,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        // Display movie list
        DisplayMovieList(navController = navController, listOfMovies = listOfMovies, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)
    }
}

@Composable
fun DisplayMovieList(
    navController: NavHostController,
    listOfMovies: List<Movie>,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            // Display title text
            DisplayTitleText(text = "Movie App")
            // Display row of film posters
            DisplayRowFilmPosters(movie1 = listOfMovies[0], movie2 = listOfMovies[1], navController = navController, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)
            DisplayRowFilmPosters(movie1 = listOfMovies[2], movie2 = listOfMovies[3], navController = navController, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)
        }
    }
}

@Composable
fun DisplayTitleText(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp , bottom = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(color = Color.Black, fontSize = 30.sp, textAlign = TextAlign.Center),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

fun convertMinutesToDuration(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60

    val hoursString = if (hours > 0) {
        "$hours h"
    } else {
        ""
    }

    val minutesString = if (remainingMinutes > 0) {
        " $remainingMinutes min"
    } else {
        ""
    }

    return "$hoursString$minutesString"
}
@Composable
fun DisplayRowFilmPosters(
    movie1: Movie,
    movie2: Movie,
    navController: NavHostController,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display film poster for first movie
        DisplayFilmPoster(movie = movie1, navController = navController, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)

        // Display film poster for second movie
        DisplayFilmPoster(movie = movie2, navController = navController, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)
    }
}
@Composable
fun DisplaySeatsRemaining(movie: Movie) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_airline_seat_recline_normal_24),
            contentDescription = "Seat Icon",
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = "${movie.seats_remaining} seats remaining",
            style = TextStyle(color = Color.White, fontSize = 14.sp, textAlign = TextAlign.Center)
        )
    }
}

@Composable
fun DisplayFilmPoster(
    movie: Movie,
    navController: NavHostController,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit,
) {
    Box(
        modifier = Modifier
            .size(200.dp, 350.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            // Display movie image
            Image(
                painter = painterResource(id = movie.image),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(160.dp, 160.dp)
                    .clickable(onClick = {
                        val updatedMovies = movieViewModel.listOfMovies.map {
                            if (it == movie) {
                                it.copy(seats_remaining = movie.seats_remaining)
                            } else {
                                it
                            }
                        }
                        onMoviesUpdated(updatedMovies)
                        navController.navigate("movieScreen/${movie.name}")
                    },),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )

            // Display movie name
            Text(
                text = movie.name,
                style = TextStyle(color = Color.White, fontSize = 20.sp, textAlign = TextAlign.Center),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Display movie duration
            Text(
                text = convertMinutesToDuration(movie.running_time_mins),
                style = TextStyle(color = Color.White, fontSize = 15.sp, textAlign = TextAlign.Center),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Display seats remaining
            DisplaySeatsRemaining(movie = movie)

            // Display watch button
            DisplayWatchButton(navController = navController, movie = movie, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)
        }
    }
}

@Composable
fun DisplayWatchButton(
    navController: NavHostController,
    movie: Movie,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit
) {
    Button(
        onClick = {
            val updatedMovies = movieViewModel.listOfMovies.map {
                if (it == movie) {
                    it.copy(seats_remaining = movie.seats_remaining)
                } else {
                    it
                }
            }
            onMoviesUpdated(updatedMovies)
            navController.navigate("movieScreen/${movie.name}")
        },
        modifier = Modifier
            .padding(16.dp)
            .height(48.dp)
            .fillMaxWidth()
            .background(Color.Black, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
    ) {
        Text(
            text = "WATCH",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// --------------------------------------------------Movie Screen Display UI------------------------------------------------------------
@Composable
fun MovieScreenDisplayUI(
    movie: Movie,
    navController: NavHostController,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit
) {

    // Column to hold all composables vertically
    ColumnLayout(movie = movie, navController = navController, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)
}

@Composable
fun ColumnLayout(
    movie: Movie,
    navController: NavHostController,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        // Display movie name
        DisplayMovieName(movie = movie,navController)

        // Display movie image
        DisplayMovieImage(movie = movie)

        // Display starring actors
        DisplayStarringActors(movie = movie)

        // Display movie duration
        DisplayMovieDuration(movie = movie)

        // Display movie description
        DisplayMovieDescription(movie = movie)

        // Row to hold seats information and selectors
        DisplaySeatsInfoAndSelectors(movie = movie, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)

        // Add spacing
        AddSpacer()

        // Submit button
        DisplaySubmitButton(navController = navController)
    }
}

@Composable
fun DisplayMovieName(movie: Movie, navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 16.dp, end = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow_svgrepo_com),
            contentDescription = "Back",
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = { navController.navigate("mainMenu") },),
        )
        Text(
            text = movie.name,
            style = TextStyle(color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.width(30.dp))
    }
}

@Composable
fun DisplayMovieImage(movie: Movie) {
    Image(
        painter = painterResource(id = movie.image),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}

@Composable
fun DisplayStarringActors(movie: Movie) {
    Text(
        text = "Starring: ${movie.starring.joinToString()}",
        style = TextStyle(color = Color.Black, fontSize = 16.sp),
        modifier = Modifier.padding(top = 15.dp, bottom = 7.dp)
    )
}

@Composable
fun DisplayMovieDuration(movie: Movie) {
    Text(
        text = "Duration: ${convertMinutesToDuration(movie.running_time_mins)}",
        style = TextStyle(color = Color.Black, fontSize = 20.sp),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun DisplayMovieDescription(movie: Movie) {
    Text(
        text = movie.description,
        style = TextStyle(color = Color.Black, fontSize = 20.sp),
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun DisplaySeatsInfoAndSelectors(
    movie: Movie,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Display seats remaining
        DisplaySeatsRemainingMovieScreen(movie = movie)

        // Display minus selector
        DisplayMinusSelector(movie = movie, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)

        // Display selected seats
        DisplaySelectedSeats(movie = movie)

        // Display plus selector
        DisplayPlusSelector(movie = movie, movieViewModel = movieViewModel, onMoviesUpdated = onMoviesUpdated)
    }
}

@Composable
fun DisplaySeatsRemainingMovieScreen(movie: Movie) {
    if(movie.seats_remaining == 0)
    {
    Text(
        text = "Seats Remaining: ${movie.seats_remaining}",
        style = TextStyle(color = Color.Black, fontSize = 20.sp),
    )}
}

@Composable
fun DisplayMinusSelector(
    movie: Movie,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit
) {
    if (movie.seats_selected > 0 || movie.seats_remaining != 0) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.minus_svgrepo_com),
            contentDescription = "minus",
            modifier = Modifier
                .size(50.dp)
                .clickable(onClick = {
                    if (movie.seats_selected > 0) {
                        movie.seats_selected--
                        val updatedMovies = movieViewModel.listOfMovies.map {
                            if (it == movie) {
                                it.copy(seats_remaining = movie.seats_remaining + 1)
                            } else {
                                it
                            }
                        }
                        onMoviesUpdated(updatedMovies)
                    }
                })
        )
    }}
}

@Composable
fun DisplaySelectedSeats(movie: Movie) {
    Text(
        text = "${movie.seats_selected}",
        style = TextStyle(color = Color.Black, fontSize = 30.sp),
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
fun DisplayPlusSelector(
    movie: Movie,
    movieViewModel: MovieViewModel,
    onMoviesUpdated: (List<Movie>) -> Unit
) {
    if (movie.seats_remaining != 0) {
    Box(
        modifier = Modifier
            .clickable {
                if (movie.seats_remaining > 0) {
                    movie.seats_selected++
                    val updatedMovies = movieViewModel.listOfMovies.map {
                        if (it == movie) {
                            it.copy(seats_remaining = movie.seats_remaining - 1)
                        } else {
                            it
                        }
                    }
                    onMoviesUpdated(updatedMovies)
                }
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.plus_svgrepo_com),
            contentDescription = "plus",
            modifier = Modifier
                .size(50.dp)
        )
    }}
}

@Composable
fun AddSpacer() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun DisplaySubmitButton(navController: NavHostController) {
    Button(
        onClick = { navController.navigate("mainMenu") },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        )
    ) {
        Text(
            text = "Submit",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// --------------------------------------------------Movie Screen ------------------------------------------------------------

@Composable
fun MovieScreen(navController: NavHostController,movie: Movie,listOfMovies: List<Movie>,
                  movieViewModel: MovieViewModel,
                  onMoviesUpdated: (List<Movie>) -> Unit) {

    MovieScreenDisplayUI(movie = movie, navController,movieViewModel,onMoviesUpdated)
}

