#!/bin/bash
SCRIPT_DIR=$(dirname "$(realpath "$0")")

echo "Diretório é $SCRIPT_DIR"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/world/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/world_nether/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/world_the_end/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/newbie_world/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/newbie_world_nether/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/newbie_world_the_end/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/town_world/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/plugins/Plugin/permission/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/plugins/Plugin/player_data/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/plugins/Plugin/towns.dat"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/plugins/Plugin/raw_material_map.dat"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/plugins/Plugin/block_data"
