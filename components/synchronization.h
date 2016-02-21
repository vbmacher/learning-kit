/* 
 * File:   synchronization.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 14:37
 */

#ifndef SYNCHRONIZATION_H
#define	SYNCHRONIZATION_H

#include <boost/thread/mutex.hpp>
#include <boost/thread/locks.hpp>
#include <SDL_stdinc.h>

namespace github {
    namespace pong {
        
        template<typename T>
        class Locked {
            volatile T var;
            mutable boost::mutex varMutex;
            
        public:
            Locked(T variable) : var(variable) {}
            
            Locked<T> &operator=(const T& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                var = value;
                return *this;
            }
            
            Locked<T> &operator+=(const Locked<T>& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                var += (T)value;
                return *this;
            }
            
            Locked<T> &operator+(const Locked<T>& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return Locked<T>(var + (T)value);
            }
            
            T operator+(const T& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var + value;
            }
            
            Locked<T> &operator-(const Locked<T>& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return Locked<T>(var - (T)value);
            }

            T operator-(const T& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var - value;
            }
            
            Locked<T> &operator*(const Locked<T>& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return Locked<T>(var * (T)value);
            }
            
            bool operator==(const T& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var == value;
            }
            
            bool operator==(const Locked<T>& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var == (T)value;
            }
            
            bool operator!=(const T& value) const {
                return !(*this == value);
            }
            
            bool operator!=(const Locked<T>& value) const {
                return !(*this == value);
            }
            
            bool operator<=(const T& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var <= value;
            }
            
            bool operator>=(const T& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var >= value;
            }
            
            operator T() const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var;
            }
        };

    }
}

#endif	/* SYNCHRONIZATION_H */

