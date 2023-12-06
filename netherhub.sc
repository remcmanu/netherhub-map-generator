__config() -> {
  'stay_loaded' -> true,
  'commands' -> {
    'generate_map <from> <to> <water_block>' -> '_generate_map',
    'switch' -> '_switch'
  },
  'arguments' -> {
    'from' -> {
      'type' -> 'pos'
    },
    'to' -> {
      'type' -> 'pos'
    },
    'water_block' -> { 
      'type' -> 'block', 
      'suggest' -> block_list()
    }
  }
};

// note: top() is always a block above, hence '- 1'

_generate_map(pos1,pos2,water_block) -> (
  if (pos1:1 != pos2:1,
    __error('Map boundaries must be on the same Y-level.');
  );
  
  // generate water layer
  volume(pos1, pos2,
    set(pos(_), water_block);
  );

  // for each block of the map
  volume(pos1, pos2,
    pos_nether = pos(_);

    average = _get_elevation_and_block_overworld(pos_nether);

    if (average:1 == 'water',
      average:1 = water_block;
    );

    print(player(),format('p ' + average));

    // get rid of sea level water with if (> 1)
    if (average:0 > 1,
      // loop through column placing average block from water_level + 1 to average elevation
      volume(pos_nether:0, pos_nether:1 + 1, pos_nether:2, pos_nether:0, pos_nether:1 + 1 + (average:0 / 8), pos_nether:2,
        without_updates(
          set(pos(_), average:1);
        );
      );
    )
  );
);


_get_elevation_and_block_overworld(pos_nether) -> (
  // top left corner of a half-chunk in the overworld
    overworld_start_pos = pos_nether * 8;

  // variable to store the average elevation of the half-chunk
    total_elevation = 0;

  // variable to store the top blocks of the half-chunk
    block_count_map = {};

  // iterate through each column, getting elevation and block
  volume(overworld_start_pos:0, 0, overworld_start_pos:2, overworld_start_pos:0 + 7, 0, overworld_start_pos:2 + 7,
      overworld_pos = pos(_);

      y_value = in_dimension('overworld', top('terrain', overworld_pos)) - 1; // top is off by 1
      
      // elevation
      level_above_sea = y_value - 62;
      // remove negative values
      if (level_above_sea < 0,
        level_above_sea = 0;
      );
      total_elevation += level_above_sea;
      
      // block id
      block_id = in_dimension('overworld',block(overworld_pos:0, y_value, overworld_pos:2));
      put(block_count_map, str(block_id), get(block_count_map, str(block_id)) + 1);
    );

    // average elevation
    average_elevation = total_elevation / 64;
    // most common block
    most_common_block_val = max(values(block_count_map));
    most_common_block_id = first(block_count_map, block_count_map:_ == most_common_block_val);
    
    print(player(),format('y ' + 
      str('DEBUG\noverworld position: [%s, %s]\ntotal elevation: %s\naverage_elevation: %s\nblock_count_map:\n%s\nmost_common_block_count: %s\nmost_common_block_id: %s\n', overworld_pos:0, overworld_pos:2, total_elevation, average_elevation, block_count_map, most_common_block_val, most_common_block_id)));

    return_block = [average_elevation, most_common_block_id];
);


_switch() -> ( 
    loc = query(player(),'pos');
    print(player(), str('location: %s, %s, %s', loc:0, loc:1, loc:2));
    dim = query(player(), 'dimension');
    print(player(), str('dimension: %s', dim));
    if(
      dim == 'the_nether',
      y = ((loc:1 - 128) * 8) + 62;
      (run(str('execute in %s run tp %s %s %s %s', 'overworld', player(), loc:0 * 8, y, loc:2 * 8))),
      y = ((loc:1 - 62) / 8) + 128;
      run(str('execute in %s run tp %s %s %s %s', 'the_nether', player(), loc:0 / 8, y, loc:2 / 8));
    )
);

__error(message) -> (
  exit(print(player(), format('r ' + message)));
);
