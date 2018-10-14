# The Challenge

## On Using Third Party Libraries

To note, the project uses one library, Picasso to cache and store images downloaded from the profile urls. The
reason why I did this is because image caching and downloading for a scrolling view (CollectionView, RecyclerView) is a 
notoriously tricky problem on both major mobile platforms (Android and iOS). Though implementing a custom
solution is not impossible, but would take a substantial amount of time.

## Other Notes 

I did use more modern architecture to structure my solution, notably, `ViewModels`.  

The requirement that images are not re-downloaded each time the app runs made me consider data 
persistence for an offline state. I considered using the library Room and traditional SQLite to store
the objects after the fetch. Room, although faster to implement has some drawbacks when it comes to
establishing relationships between objects, in this case, `Badges` and the `StackProfile`.

You can read more about this here: [object references](https://developer.android.com/training/data-storage/room/referencing-data#understand-no-object-references)

On the other hand, using SQLite directly would require a bit of boiler plate code. As a result, because the challenge 
only asked for the first page, I decided to merely save the JSON as a String into `SharedPreference`,
which can be parsed for object instantiation in an offline state.

## Nice to Haves

- Databinding
- A more elaborate ViewHolder to display more information.