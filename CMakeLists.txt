cmake_minimum_required (VERSION 2.8)
project (Pong)

# Packages
find_package(SDL)
find_package(GTest)

set(Boost_USE_STATIC_LIBS ON)
set(Boost_USE_MULTITHREADED ON)
set(Boost_USE_STATIC_RUNTIME ON)

find_package(Boost 1.52 COMPONENTS system thread)

include_directories(${Boost_INCLUDE_DIRS})

if (SDL_FOUND)
    include_directories(${SDL_INCLUDE_DIR})
endif()

add_subdirectory(src)
add_subdirectory(graphics)


