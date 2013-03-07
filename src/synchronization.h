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

namespace github {
    namespace pong {
        
        template<typename T>
        class Locked {
            volatile T var;
            boost::mutex varMutex;
            
        public:
            Locked(T variable) : var(variable) {}
            
            Locked &operator=(const T& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                var = value;
                return *this;
            }
            
            bool operator==(const T& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var == value;
            }
            
            bool operator!=(const T& value) const {
                return !(*this == value);
            }
            
            operator bool() {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var;
            }
                
        };
    }
}

#endif	/* SYNCHRONIZATION_H */

