#!/bin/bash
SCRIPT_DIR=$(dirname "$(realpath "$0")")

echo "Diretório é $SCRIPT_DIR"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/world/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/world_nether/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/world_the_end/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/town_world/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/plugins/Plugin/player_data/"
rm -rf "$SCRIPT_DIR/local/cidade-virtual/server/plugins/Plugin/towns.dat"
