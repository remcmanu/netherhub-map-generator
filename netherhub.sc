__config() -> {
  'stay_loaded' -> true,
  'commands' -> {
    'switch' -> '_switch',
    'raise_area <from> <to> <water_level>' -> '_raise_area',
  },
  'arguments' -> {
    'from' -> {
      'type' -> 'pos'
    },
    'to' -> {
      'type' -> 'pos'
    },
    'water_level' -> { 
      'type' -> 'int', 
      'min' -> 128, 
      'max' -> 255, 
      'suggest' -> [128]}
  }
};

// note: top() is always a block above, hence '- 1'

_raise_area(pos1, pos2, water_level) -> (
    // for each block in the nether
    volume(pos1, pos2,
      position = pos(_);
      print(player(), str('(_raise_area) position: %s', position));
      
      average_level = _get_average_elevation_in_overworld(position);
      print(player(), str('(_raise_area) final average_level: %s', average_level));
      
      average_level_scaled = average_level / 8;
      print(player(), str('(_raise_area) average_level_scaled: %s', average_level_scaled));
      
      if (average_level_scaled >= 1,
        print(player(), str('(SHOULD RAISE) block: %s', block(position:0, water_level + 1, position:2)));
        set(position:0, water_level + average_level_scaled + 1, position:2, block(position:0, water_level + 1, position:2)); // later most common block
      );
    );
);

_get_average_elevation_in_overworld(position) -> (
    // top left corner of a half-chunk in the overworld
    overworld_start_pos = position * 8;
    print(player(), str('(AVG ELEV) overworld_start_pos: %s, %s', overworld_start_pos:0, overworld_start_pos:2));
    
    // variable to store the average elevation of the half-chunk
    average_level = 0;

    // check each block's height above water level
    volume(overworld_start_pos:0, 0, overworld_start_pos:2, overworld_start_pos:0 + 7, 0, overworld_start_pos:2 + 7,
      overworld_pos = pos(_);
      print(player(), str('(AVG ELEV) overworld_pos: %s, %s', overworld_pos:0, overworld_pos:2));
      
      level_above_sea = in_dimension('overworld', top('terrain', overworld_pos)) - 63;
      print(player(), str('(AVG ELEV) level_above_sea: %s', level_above_sea));

      // remove negative values
      if (level_above_sea < 0,
        level_above_sea = 0;
      );

      average_level += level_above_sea;
      print(player(), str('(AVG ELEV) new average_level: %s', average_level));
    );
    print(player(), str('(AVG ELEV) final average_level: %s', average_level / 64));
    average_level = average_level / 64;
);


_switch() -> ( 
    loc = query(player(),'pos');
    print(player(), str('location: %s, %s, %s', loc:0, loc:1, loc:2));
    dim = query(player(), 'dimension');
    print(player(), str('dimension: %s', dim));
    if(
      dim == 'the_nether',
      (run(str('execute in %s run tp %s %s %s %s', 'overworld', player(), loc:0 * 8, 150, loc:2 * 8))),
      run(str('execute in %s run tp %s %s %s %s', 'the_nether', player(), loc:0 / 8, 210, loc:2 / 8));
    )
);
